package com.revolut.bank.infrustructure.persistance;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountRepositoryJpa implements AccountRepository {

    private Provider<EntityManager> em;

    @Inject
    public AccountRepositoryJpa(final Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public Optional<Account> getByNo(String accountNo) {
        Account account;

        try {
            account= em.get().createNamedQuery("Account.findByAccountNo", Account.class)
                    .setParameter("accountNo", accountNo)
                    .getSingleResult();
        } catch (NoResultException e) {
            account = null;
        }

        return Optional.ofNullable(account);
    }

    @Override
    public List<Account> getAll() {
        return em.get().createNamedQuery("Account.findAll", Account.class)
                .getResultList();
    }

    @Override
    public void store(Account account) {
        em.get().getTransaction().begin();
        em.get().persist(account);
        em.get().getTransaction().commit();
    }
}
