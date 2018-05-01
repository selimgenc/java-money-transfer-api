package com.revolut.bank.infrustructure.persistance;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;

import javax.inject.Singleton;
import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class AccountRepositoryInMem implements AccountRepository {
    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public AccountRepositoryInMem() {
        accounts.put("account1", new Account("account1", new BigDecimal(100), "test"));
        accounts.put("account2", new Account("account2", new BigDecimal(100), "test"));
    }

    @Override
    public Optional<Account> getByNo(String accountNo) {
        return Optional.ofNullable(accounts.get(accountNo));
    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void store(Account account) {
        accounts.put(account.getAccountNo(), account);
    }
}
