package com.revolut.bank.application.internal;

import com.revolut.bank.application.AccountService;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.events.account.AccountCreatedEvent;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.Validate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());

    private AccountRepository accountRepository;
    private EventHandlerService eventHandlerService;

    @Inject
    public AccountServiceImpl(final AccountRepository accountRepository, final EventHandlerService eventHandlerService) {
        this.accountRepository = accountRepository;
        this.eventHandlerService = eventHandlerService;
    }

    private String nextAccountNumber(){
        return UUID.randomUUID().toString(); // just for simplicity
    }

    @Override
    public String createNewAccount(String customer, BigDecimal credit, String user) throws DomainException {
        String accountNo = nextAccountNumber();
        Validate.isTrue(credit.doubleValue() > 0, "account.credit.wrong", "Credit cant  be less than 0", (Object[]) null);
        //check user validation etc..

        Account account = new Account.Builder(accountNo, credit, customer).build();
        accountRepository.store(account);

        logger.log(Level.INFO, "Account {1} created ", accountNo);
        //Shout out new account created.
        eventHandlerService.send(new AccountCreatedEvent(account, new Date()));

        return accountNo;

    }


}
