package com.revolut.bank.domain.transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> getTransactionById(String id);
    List<Transaction> getAll();
}
