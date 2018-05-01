package com.revolut.bank.domain.handling;

/**
 * this is exception for differing source of business rules
 */
public class DomainException extends Exception {
    private static final long serialVersionUID = 1L;
    private String description;

    public DomainException(String message, String description) {
        super(message);
        this.description = description;
    }

    public DomainException(String message)
    {
        super(message);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
