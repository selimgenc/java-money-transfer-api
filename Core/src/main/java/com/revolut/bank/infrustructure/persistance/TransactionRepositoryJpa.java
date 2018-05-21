package com.revolut.bank.infrustructure.persistance;

import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.domain.transaction.TransactionRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Singleton
public class TransactionRepositoryJpa implements TransactionRepository {

    private Provider<EntityManager> em;

    @Inject
    public TransactionRepositoryJpa(final Provider<EntityManager> em) {
        this.em =em;
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) {
        Transaction transaction;
        try {
            transaction = em.get().createNamedQuery("Transaction.findByTransactionNo", Transaction.class)
                    .setParameter("transactionNo", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            transaction = null;
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public List<Transaction> getAll() {
        return em.get().createNamedQuery("Transaction.findAll", Transaction.class)
                .getResultList();
    }

}
