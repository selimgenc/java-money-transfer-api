package com.revolut.bank.domain.account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository{
    Optional<Account> getByNo(String accountNo);
    List<Account> getAll(); //todo instead partial get
    void store(Account account);

}
