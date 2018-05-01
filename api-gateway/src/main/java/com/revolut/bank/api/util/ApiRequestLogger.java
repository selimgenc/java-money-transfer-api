package com.revolut.bank.api.util;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApiLogger
@Provider
public class ApiRequestLogger implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger logger = Logger.getLogger(ApiRequestLogger.class.getName());
    @Override
    public void filter(ContainerRequestContext context) {
        String msg = "API Request: " + context.getUriInfo().getPath() + "\t" +
                "Time: " + context.getDate() + "\t" +
                "Method: " + context.getMethod() + "\t" +
                "User: " + context.getHeaderString("user") + "\t" +
                "Client App: " + context.getHeaderString("client") + "\t";
        logger.log(Level.FINE, msg);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(!responseContext.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            String msg =
                    "Response Status: " + responseContext.getStatusInfo().toString() + "\t" +
                    "Response Time: " + responseContext.getDate() + "\t" +
                    "Request: " + requestContext.getUriInfo().getPath()+ "\t" +
                    "Time: " + requestContext.getDate() + "\t" +
                    "Method: " + requestContext.getMethod() + "\t" +
                    "User: " + requestContext.getHeaderString("user") + "\t" +
                    "Client App: " + requestContext.getHeaderString("client") + "\t";
            logger.log(Level.WARNING, msg);
        }
    }
}