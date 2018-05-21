package com.revolut.bank.application;

import com.revolut.bank.domain.handling.DomainException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service for transactions
 */
public interface TransactionService {
    String transfer(String fromAccountNo, String toAccountNo, BigDecimal amount, String user) throws DomainException;
}
