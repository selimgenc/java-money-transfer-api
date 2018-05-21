package com.revolut.bank.application.internal;

import com.revolut.bank.application.TransactionService;
import com.revolut.bank.application.UnitOfWork;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.events.transaction.TransactionCreatedEvent;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.Validate;
import com.revolut.bank.domain.transaction.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

    private AccountRepository accountRepository;
    private EventHandlerService eventHandlerService;
    private UnitOfWork unitOfWork;

    @Inject
    public TransactionServiceImpl(AccountRepository accountRepository, EventHandlerService eventHandlerService, UnitOfWork unitOfWork) {
        this.accountRepository = accountRepository;
        this.eventHandlerService = eventHandlerService;
        this.unitOfWork = unitOfWork;
    }

    private String nextTransactionNumber(){
        return UUID.randomUUID().toString(); // just for simplicity. //todo get from repository
    }

    //Since Google Guice doesnt support javax.transaction.Transactional and there is a restriction using Spring framework, I've implemented transactions manually with UnitOfWork
    //With a little effort system can be adapted any Java standard framework and library, I dont want to depend application layer to any vendor specific framework.
    @Override
    public String transfer(String fromAccountNo, String toAccountNo, BigDecimal amount, String user) throws DomainException {
        Optional<Account> from = accountRepository.getByNo(fromAccountNo);
        Optional<Account> to = accountRepository.getByNo(toAccountNo);
        Validate.notNull(from, "account.notfound", "Account with {1} number not found for transfer",fromAccountNo);
        Validate.notNull(to, "account.notfound", "Account with {1} number not found for transfer", toAccountNo);
        Validate.hasBalance(from.get(), amount);
        Validate.isTrue(!fromAccountNo.equalsIgnoreCase(toAccountNo),"transfer.samAccount", "Cannot transfer to same account");

        String transactionNumber = nextTransactionNumber();
        Transaction transaction = new Transaction.Builder(transactionNumber, fromAccountNo, toAccountNo, amount, new Date())
                .user(user)
                .build();
        from.get().withdraw(amount);
        to.get().deposit(amount);
        try{
            unitOfWork.beginTransaction();
            unitOfWork.registerNew(transaction);
            unitOfWork.registerModified(from.get());
            unitOfWork.registerModified(to.get());
            unitOfWork.commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Transaction exception", e);
            throw new DomainException("account.transfer.failed");
        }


        logger.log(Level.INFO, "Transaction {1} created ", transaction.toString());
        System.out.println(transaction);
        //Shout out new transaction happened
        eventHandlerService.send(new TransactionCreatedEvent(transaction, new Date()));

        return transactionNumber;
    }
}
