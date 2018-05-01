package com.revolut.bank.application;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.domain.transaction.TransactionRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransactionServiceTest extends ApplicationTestBase{

    @Test
    public void test_createTransfer_Successful() throws DomainException {
        //Prepare
        AccountRepository accountRepository = injector.getInstance(AccountRepository.class);
        List<Account> accounts = accountRepository.getAll();
        Account account1 = accounts.get(0);
        Account account2 = accounts.get(1);
        BigDecimal account1StartCredit = account1.getCredit();
        BigDecimal account2StartCredit = account2.getCredit();

        String accountNoFrom = account1.getAccountNo();
        String accountNoTo = account2.getAccountNo();

        TransactionService transactionService = injector.getInstance(TransactionService.class);
        int amaount = 10;
        //Test
        String transferID = transactionService.transfer(accountNoFrom, accountNoTo, new BigDecimal(amaount), "dd", "Internet Bank");

        //Assert
        TransactionRepository transactionRepository = injector.getInstance(TransactionRepository.class);
        Optional<Transaction> transactionById = transactionRepository.getTransactionById(transferID);

        Assert.assertTrue(transactionById.isPresent());
        Assert.assertEquals(transferID, transactionById.get().getTransactionNo());

        //Assert Acounts Credits
        Optional<Account> optionalAccountFrom = accountRepository.getByNo(accountNoFrom);
        Optional<Account> optionalAccountTo = accountRepository.getByNo(accountNoTo);

        Assert.assertEquals(new BigDecimal(account1StartCredit.intValue() - amaount), optionalAccountFrom.get().getCredit());
        Assert.assertEquals(new BigDecimal(account2StartCredit.intValue() + amaount), optionalAccountTo.get().getCredit());
    }

    @Test
    public void test_transfer_account_not_found() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.notfound");

        TransactionService transactionService = injector.getInstance(TransactionService.class);
        transactionService.transfer("someAccountNumber", "someAccountNumber2", new BigDecimal(10), "", "");

    }


    @Test
    public void test_transfer_With_NotEnough_Balance_to_Transfer() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.transfer.notenoughbalance");

        AccountService accountService = injector.getInstance(AccountService.class);
        String accountNumberFrom = accountService.createNewAccount("New Customer 1", new BigDecimal(10), "by", "Internet Banking");
        String accountNumberTo = accountService.createNewAccount("New Customer 2", new BigDecimal(10), "by", "Internet Banking");

        TransactionService transactionService = injector.getInstance(TransactionService.class);
        transactionService.transfer(accountNumberFrom, accountNumberTo, new BigDecimal(50), "", "");

    }
    @Test
    public void test_transfer_cannot_transfer_to_itself() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("transfer.samAccount");

        AccountService accountService = injector.getInstance(AccountService.class);
        String accountNumberFrom = accountService.createNewAccount("New Customer 1", new BigDecimal(100), "by", "Internet Banking");

        TransactionService transactionService = injector.getInstance(TransactionService.class);
        transactionService.transfer(accountNumberFrom, accountNumberFrom, new BigDecimal(10), "", "");

    }


}
