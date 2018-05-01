package com.revolut.bank.application.internal;

import com.revolut.bank.application.AccountService;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.events.account.AccountCreatedEvent;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.Validate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());

    @Inject
    private AccountRepository accountRepository;
    @Inject
    private EventHandlerService eventHandlerService;

    private String nextAccountNumber(){
        return UUID.randomUUID().toString(); // just for simplicity
    }

    @Transactional
    @Override
    public String createNewAccount(String customer, BigDecimal credit, String user, String location) throws DomainException {
        String accountNo = nextAccountNumber();
        Validate.isTrue(credit.doubleValue() > 0, "account.credit.wrong", "Credit cant  be less than 0", (Object[]) null);
        //check user validation etc..

        Account account = new Account(accountNo, credit, user);
        account.setAccountNo(accountNo);
        account.setCredit(credit);
        account.setCustomer(customer);
        account.setCreatedBy(user);
        account.setCreateTime(new Date());

        accountRepository.store(account);

        logger.log(Level.INFO, "Account {1} created ", accountNo);
        //Shout out new account created.
        eventHandlerService.send(new AccountCreatedEvent(account, new Date(), location));

        return accountNo;

    }


}
