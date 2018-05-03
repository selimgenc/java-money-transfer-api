package com.revolut.bank.api.account;

import com.revolut.bank.api.account.dto.AccountDto;
import com.revolut.bank.api.exception.ApiErrorMessage;
import com.revolut.bank.api.exception.ApiException;
import com.revolut.bank.api.util.ApiLogger;
import com.revolut.bank.api.util.HateosResource;
import com.revolut.bank.application.AccountService;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/v1/account")
public class AccountResource {

    @Inject
    private AccountService accountService;
    @Inject
    private AccountRepository accountRepository;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAccount(@Suspended final AsyncResponse asyncResponse, @PathParam("accountNumber") String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.getByNo(accountNumber);
        if(!optionalAccount.isPresent()){
            throw new ApiException(new ApiErrorMessage("ERR-A-01", "The Required Account not found", null, Response.Status.NOT_FOUND));
        }

        AccountDto accountDto = AccountDto.createFromAccount(optionalAccount.get());
        asyncResponse.resume(Response.ok(accountDto).build());

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void accounts(@Suspended final AsyncResponse asyncResponse) {
        List<Account> accounts = accountRepository.getAll();

        if(accounts.isEmpty()){
            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }
        List<JsonObject> objects = accounts.stream()
                .map(AccountDto::createFromAccount)
                .map(dto -> createHateosJson(dto, uriInfo))
                .collect(Collectors.toList());

        asyncResponse.resume(
                Response.ok().entity(objects.toString()).build()
        );
    }

    @ApiLogger
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createAccoount(@Suspended final AsyncResponse asyncResponse, AccountDto accountDto, @Context HttpHeaders headers) throws DomainException {
        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
        String user = requestHeaders.getFirst("user");
        String locaiton= requestHeaders.getFirst("location");

        String newAccountNumber = accountService.createNewAccount(accountDto.getCustomer(), accountDto.getCredit(), user, locaiton);
        URI location = HateosResource.createAccountUri(newAccountNumber, uriInfo);
        asyncResponse.resume(Response.created(location).build());

    }

    private static JsonObject createHateosJson(AccountDto dto, UriInfo uriInfo){
        return Json.createObjectBuilder()
                .add("accountNumber", dto.getAccountNumber())
                .add("customer", dto.getCustomer())
                .add("credit", dto.getCredit())
                .add("_links", Json.createObjectBuilder()
                        .add("Self", Json.createObjectBuilder()
                                .add("href", HateosResource.createAccountUri(dto.getAccountNumber(), uriInfo).toString()))
                )
                .build();
    }

}


