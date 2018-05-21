package com.revolut.bank.api.transaction;

import com.revolut.bank.api.exception.ApiErrorMessage;
import com.revolut.bank.api.exception.ApiException;
import com.revolut.bank.api.transaction.dto.TransactionDto;
import com.revolut.bank.api.util.ApiLogger;
import com.revolut.bank.api.util.HateosResource;
import com.revolut.bank.application.AccountService;
import com.revolut.bank.application.TransactionService;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.domain.transaction.TransactionRepository;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/v1/transaction")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private TransactionService transactionService;

    @Inject
    private AccountService accountService;

    @Context
    private UriInfo uriInfo;

    @PostConstruct // for testing
    public void init() throws DomainException {
        String accountNo1 = accountService.createNewAccount("Customer-1", new BigDecimal(100), "api");
        String accountNo2 = accountService.createNewAccount("Customer-2", new BigDecimal(100), "api");
        transactionService.transfer(accountNo1, accountNo2, new BigDecimal(2), "ss");
        transactionService.transfer(accountNo2, accountNo1, new BigDecimal(5), "bb");

    }

    @GET
    @Path("/{transactionNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getTransferByNumber(@Suspended final AsyncResponse asyncResponse, @PathParam("transactionNumber") String transactionNumber) {
        List<Transaction> all = transactionRepository.getAll();
        Optional<Transaction> first = all.stream().filter(t -> t.getTransactionNo().equalsIgnoreCase(transactionNumber)).findFirst();
        if(first != null){
            System.out.println(first);
        }
        Optional<Transaction> optionalTransaction = transactionRepository.getTransactionById(transactionNumber);
        if(!optionalTransaction.isPresent()){
            throw new ApiException(new ApiErrorMessage("ERR-TR-01", "The Required Transfer not found", null, Response.Status.NOT_FOUND));
        }
        TransactionDto transactionDto = TransactionDto.buildFromEntity(optionalTransaction.get());

        URI selfUri = HateosResource.createTransactionUri(transactionDto.getTransactionNumber(), uriInfo);
        URI fromUri = HateosResource.createAccountUri(transactionDto.getFrom(), uriInfo);
        URI toUri = HateosResource.createAccountUri(transactionDto.getTo(), uriInfo);
        JsonObject jsonObject = asJsonObject(transactionDto, selfUri, fromUri, toUri);
        asyncResponse.resume(
                Response.ok(jsonObject.toString())
                        .link(selfUri, "self")
                        .link(fromUri, "From")
                        .link(toUri, "To")
                        .build()
        );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void  accounts(@Suspended final AsyncResponse asyncResponse) {
        List<Transaction> transactions = transactionRepository.getAll();
        if(transactions.isEmpty()){
            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }
        List<JsonObject> objects = transactions.stream()
                .map(TransactionDto::buildFromEntity)
                .map(dto -> asJsonObject(dto, HateosResource.createTransactionUri(dto.getTransactionNumber(), uriInfo),
                        HateosResource.createAccountUri(dto.getFrom(), uriInfo),
                        HateosResource.createAccountUri(dto.getTo(), uriInfo)
                ))
                .collect(Collectors.toList());
        asyncResponse.resume(Response.ok().entity(objects.toString()).build());
    }

    @ApiLogger
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createTransfer(@Suspended final AsyncResponse asyncResponse, TransactionDto dto, @Context HttpHeaders headers) throws DomainException {
        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
        String user = requestHeaders.getFirst("user");
        String transactionNumber = transactionService.transfer(dto.getFrom(), dto.getTo(), dto.getAmount(), user);
        URI location = HateosResource.createTransactionUri(transactionNumber, uriInfo);
        asyncResponse.resume(Response.created(location).build());
    }

    //region Helper Methods
    private JsonObject asJsonObject(TransactionDto dto, URI selfUri, URI fromUri, URI toUri) {
        return Json.createObjectBuilder()
                .add("transactionNumber", dto.getTransactionNumber())
                .add("amount", dto.getAmount())
                .add("time", dto.getTime().toString())
                .add("_links", Json.createObjectBuilder()
                        .add("Self", Json.createObjectBuilder()
                                .add("href", selfUri.toString()))
                        .add("From", Json.createObjectBuilder()
                                .add("href", fromUri.toString()))
                        .add("To", Json.createObjectBuilder()
                                .add("href", toUri.toString()))
                )
                .build();
    }

    //endregion

}
