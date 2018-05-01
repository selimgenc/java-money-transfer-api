package com.revolut.bank.application.internal;

import com.revolut.bank.application.TransactionService;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.events.transaction.TransactionCreatedEvent;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.Validate;
import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.domain.transaction.TransactionRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private EventHandlerService eventHandlerService;

    private String nextTransactionNumber(){
        return UUID.randomUUID().toString(); // just for simplicity
    }

    @Override
    @Transactional
    public String transfer(String fromAccountNo, String toAccountNo, BigDecimal amount, String user, String location) throws DomainException {
        Optional<Account> from = accountRepository.getByNo(fromAccountNo);
        Optional<Account> to = accountRepository.getByNo(toAccountNo);
        Validate.notNull(from, "account.notfound", "Account with {1} number not found for transfer",fromAccountNo);
        Validate.notNull(to, "account.notfound", "Account with {1} number not found for transfer", toAccountNo);
        Validate.hasBalance(from.get(), amount);
        Validate.isTrue(!fromAccountNo.equalsIgnoreCase(toAccountNo),"transfer.samAccount", "Cannot transfer to same account");

        String transactionNumber = nextTransactionNumber();
        Date time = new Date();
        Transaction transaction = new Transaction(transactionNumber, from.get(), to.get(), amount, time);
        transaction.setCreatedBy(user);
        transaction.setLocation(location);
        transaction.setType("Transfer");
        transactionRepository.store(transaction);
        withdraw(from.get(), amount);
        deposit(to.get(), amount);

        logger.log(Level.INFO, "Transaction {1} created ", transaction);

        //Shout out new account created
        eventHandlerService.send(new TransactionCreatedEvent(transaction, new Date()));

        return transactionNumber;
    }


    private void deposit(Account account, BigDecimal amount) {
        account.setCredit(account.getCredit().add(amount));
        accountRepository.store(account);
    }

    private void withdraw(Account account, BigDecimal amount) {
        account.setCredit(account.getCredit().subtract(amount));
        accountRepository.store(account);
    }


    @PostConstruct // for testing
    public void init() throws DomainException {
        List<Account> accounts = accountRepository.getAll();
        Account account1 = accounts.get(0);
        Account account2 = accounts.get(1);
        this.transfer(account1.getAccountNo(), account2.getAccountNo(), new BigDecimal(10), "bb", "Test 2");
        this.transfer(account2.getAccountNo(), account1.getAccountNo(), new BigDecimal(20), "ss", "TEst");

    }
}
