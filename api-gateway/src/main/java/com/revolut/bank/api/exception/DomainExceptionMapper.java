package com.revolut.bank.api.exception;

import com.revolut.bank.domain.handling.DomainException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/*
    We convert our domain exception to our api exception
 */
@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiErrorMessage(exception.getMessage(), exception.getDescription(), null, Response.Status.INTERNAL_SERVER_ERROR))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
