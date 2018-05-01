package com.revolut.bank.application;

import com.google.inject.*;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.internal.AccountServiceImpl;
import com.revolut.bank.application.internal.TransactionServiceImpl;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.transaction.TransactionRepository;
import com.revolut.bank.infrustructure.handling.RxEventServiceImpl;
import com.revolut.bank.infrustructure.persistance.AccountRepositoryInMem;
import com.revolut.bank.infrustructure.persistance.TransactionRepositoryInMem;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ApplicationTestBase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    protected static Injector injector;

    @BeforeClass
    public static void before() {
        injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(EventHandlerService.class).to(RxEventServiceImpl.class);
                binder.bind(AccountRepository.class).to(AccountRepositoryInMem.class);
                binder.bind(AccountService.class).to(AccountServiceImpl.class);

                binder.bind(TransactionRepository.class).to(TransactionRepositoryInMem.class);
                binder.bind(TransactionService.class).to(TransactionServiceImpl.class);
            }
        });


    }

}
