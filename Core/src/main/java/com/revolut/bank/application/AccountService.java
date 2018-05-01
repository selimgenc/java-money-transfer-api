package com.revolut.bank.application;

import com.revolut.bank.domain.handling.DomainException;

import java.math.BigDecimal;

public interface AccountService {
    String createNewAccount(String customer, BigDecimal credit, String user, String location) throws DomainException;

}
