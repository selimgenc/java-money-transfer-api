package com.revolut.bank.api.util;

import com.revolut.bank.api.account.AccountResource;
import com.revolut.bank.api.transaction.TransactionResource;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Singleton
@Path("hateos")
@Produces(MediaType.APPLICATION_JSON)
public class HateosResource {

    @Context
    private ResourceContext context;

    @Path("/transaction")
    public TransactionResource transactions() {
        return context.getResource(TransactionResource.class);
    }


    @Path("/account")
    public AccountResource accounts() {
        return context.getResource(AccountResource.class);
    }

    public static URI createTransactionUri(String transactionNumber, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(TransactionResource.class)
                .path(transactionNumber)
                .build();
    }

    public  static URI createAccountUri(String accountNumber, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(AccountResource.class)
                .path(accountNumber)
                .build(accountNumber);
    }
}
