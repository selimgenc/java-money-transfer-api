package com.revolut.bank.application;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;


public class AccountServiceTest extends ApplicationTestBase {

    @Test
    public void test_createAccount_Successfull() throws DomainException {
        AccountService accountService = injector.getInstance(AccountService.class);
        String accountNo = accountService.createNewAccount("slmgnc", new BigDecimal(10), "dd");

        AccountRepository accountRepository = injector.getInstance(AccountRepository.class);
        Optional<Account> account = accountRepository.getByNo(accountNo);

        Assert.assertTrue(account.isPresent());
        Assert.assertEquals(accountNo, account.get().getAccountNo());
    }


    @Test
    public void test_createAccount_With_NotEnough_Credit() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.credit.wrong");

        AccountService accountService = injector.getInstance(AccountService.class);
        accountService.createNewAccount("slmgnc", new BigDecimal(-5), "dd");
    }

}
