package com.revolut.bank.api.exception;

import javax.ws.rs.core.Response;
import java.io.Serializable;

public class ApiErrorMessage implements Serializable{
    private String errorCode;
    private String description;
    private String link;
    private Response.Status statusCode;

    public ApiErrorMessage(String errorCode, String description, String link, Response.Status statusCode) {
        this.errorCode = errorCode;
        this.description = description;
        this.link = link;
        this.statusCode = statusCode;
    }

    public ApiErrorMessage() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Response.Status getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Response.Status statusCode) {
        this.statusCode = statusCode;
    }
}
