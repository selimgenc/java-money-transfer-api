package com.revolut.bank.application;

import com.revolut.bank.domain.handling.DomainException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service for transactions
 */
public interface TransactionService {
    String transfer(String fromAccountNo, String toAccountNo, BigDecimal amount, String user, String location) throws DomainException;
//    Optional<String> deposit(String accountNo, BigDecimal amount, String user, String location) throws DomainException;
//    Optional<String> withdraw(String accountNo, BigDecimal amount, String user, String location) throws DomainException;
}
