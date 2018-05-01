package com.revolut.bank.infrustructure.persistance;

import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.domain.transaction.TransactionRepository;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Singleton
public class TransactionRepositoryInMem implements TransactionRepository {
    private static final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    @Override
    public Optional<Transaction> getTransactionById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public List<Transaction> getAll() {

        return new ArrayList<>(transactions.values());
    }

    @Override
    public void store(Transaction transaction) {
        transactions.put(transaction.getTransactionNo(), transaction);
    }
}
