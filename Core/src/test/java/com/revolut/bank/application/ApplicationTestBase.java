package com.revolut.bank.application;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.internal.AccountServiceImpl;
import com.revolut.bank.application.internal.TransactionServiceImpl;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.transaction.TransactionRepository;
import com.revolut.bank.infrustructure.handling.RxEventServiceImpl;
import com.revolut.bank.infrustructure.persistance.AccountRepositoryJpa;
import com.revolut.bank.infrustructure.persistance.TransactionRepositoryJpa;
import com.revolut.bank.infrustructure.persistance.UnitOfWorkJpa;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ApplicationTestBase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    protected static Injector injector;

    @Before
    public void setup() {
        injector = Guice.createInjector(
                new Module() {
                    @Override
                    public void configure(Binder binder) {
                        binder.bind(EventHandlerService.class).to(RxEventServiceImpl.class);
                        binder.bind(AccountRepository.class).to(AccountRepositoryJpa.class);
                        binder.bind(AccountService.class).to(AccountServiceImpl.class);

                        binder.bind(TransactionRepository.class).to(TransactionRepositoryJpa.class);
                        binder.bind(TransactionService.class).to(TransactionServiceImpl.class);
                        binder.bind(UnitOfWork.class).to(UnitOfWorkJpa.class);
                    }
                    },
                new JpaPersistModule("RevolutBankUnit")
        );
        PersistService service = injector.getInstance(PersistService.class);
        service.start();
    }

//    protected static SessionFactory getSessionFactory(){
//
//        return new Configuration()
//                .configure()
//                .addAnnotatedClass(Account.class)
//                .addAnnotatedClass(Transaction.class)
////                .addPackage("com.revolut.bank.domain")
//                .buildSessionFactory();
//
//    }

}
