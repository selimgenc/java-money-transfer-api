package com.revolut.bank.api.account;

import com.revolut.bank.api.account.dto.AccountDto;
import com.revolut.bank.api.account.dto.MovementDto;
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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

        AccountDto accountDto = AccountDto.buildFromAccountWithMovements(optionalAccount.get());

        asyncResponse.resume(Response.ok(createJsonWithMovements(accountDto).toString()).build());

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
                .map(AccountDto::buildFromAccount)
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
        String newAccountNumber = accountService.createNewAccount(accountDto.getCustomer(), accountDto.getCredit(), user);
        URI location = HateosResource.createAccountUri(newAccountNumber, uriInfo);
        asyncResponse.resume(Response.created(location).build());

    }

    private static JsonObject createHateosJson(AccountDto dto, UriInfo uriInfo){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addAccount(builder, dto);
        addHateos(builder, dto, uriInfo);
        return builder.build();
    }

    private static JsonObject createJsonWithMovements(AccountDto dto){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addAccount(builder, dto);
        addMovements(builder, dto);
        return builder.build();
    }


    private static void addAccount(JsonObjectBuilder builder, AccountDto dto){
        builder.add("accountNumber", dto.getAccountNumber())
                .add("customer", dto.getCustomer())
                .add("credit", dto.getCredit());
    }

    private static void addHateos(JsonObjectBuilder builder, AccountDto dto, UriInfo uriInfo){
        builder.add("_links", Json.createObjectBuilder()
                        .add("Self", Json.createObjectBuilder()
                                .add("href", HateosResource.createAccountUri(dto.getAccountNumber(), uriInfo).toString()))
        );
    }

    private static void addMovements(JsonObjectBuilder builder,  AccountDto dto){
        JsonArrayBuilder bm = Json.createArrayBuilder();
        for (MovementDto movement : dto.getMovements()) {
            bm.add(Json.createObjectBuilder().add("type", movement.getType()).add("amount", movement.getAmount()));
        }
        builder.add("movements", bm);
    }

}


