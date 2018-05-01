package com.revolut.bank.api.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ApiException extends WebApplicationException {

    public ApiException(ApiErrorMessage errorMessage) {
        super(Response.status(errorMessage.getStatusCode())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}
