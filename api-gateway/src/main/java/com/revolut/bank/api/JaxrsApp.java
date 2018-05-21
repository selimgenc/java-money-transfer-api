package com.revolut.bank.api;

import com.revolut.bank.application.AccountService;
import com.revolut.bank.application.TransactionService;
import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.internal.AccountServiceImpl;
import com.revolut.bank.application.internal.TransactionServiceImpl;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.transaction.TransactionRepository;
import com.revolut.bank.application.UnitOfWork;
import com.revolut.bank.infrustructure.handling.RxEventServiceImpl;
import com.revolut.bank.infrustructure.persistance.AccountRepositoryJpa;
import com.revolut.bank.infrustructure.persistance.TransactionRepositoryJpa;
import com.revolut.bank.infrustructure.persistance.UnitOfWorkJpa;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class JaxrsApp extends ResourceConfig {

    public JaxrsApp() {

        // scan the resources package for our resources
        packages(getClass().getPackage().getName());

        // use @Inject to bind the StormtrooperDao
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(RxEventServiceImpl.class).to(EventHandlerService.class);
                bind(AccountRepositoryJpa.class).to(AccountRepository.class);
                bind(AccountServiceImpl.class).to(AccountService.class);

                bind(TransactionRepositoryJpa.class).to(TransactionRepository.class);
                bind(TransactionServiceImpl.class).to(TransactionService.class);

                bind(UnitOfWorkJpa.class).to(UnitOfWork.class);

                EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("RevolutBankIntegration");
                bind(emFactory).to(EntityManagerFactory.class);
                bind(emFactory.createEntityManager()).to(EntityManager.class);

            }
        });
    }


}