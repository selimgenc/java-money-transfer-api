package com.revolut.bank.persistence;

import com.revolut.bank.application.ApplicationTestBase;
import com.revolut.bank.application.UnitOfWork;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.transaction.Transaction;
import com.revolut.bank.infrustructure.persistance.UnitOfWorkJpa;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Date;

public class UnitofWorkTest extends ApplicationTestBase {

    @Test
    public void testAllRegiesteredShouldBePersistent() throws DomainException {
        //Arrange
        Account accountWouldBeModified = new Account.Builder("A1", new BigDecimal(100), "slmgnc").build();
        Transaction transactionWouldNew = new Transaction.Builder("T1", "A1", "A2", new BigDecimal(10), new Date()).user("slm").build();
        Account accountWouldBeDeleted = new Account.Builder("A2", new BigDecimal(200), "memo").build();

        EntityManagerFactory factory = injector.getInstance(EntityManagerFactory.class);
        EntityManager em = factory.createEntityManager();

        Provider<EntityManager> provider = new Provider<EntityManager>() {
            @Override
            public EntityManager get() {
                return em;
            }
        };

        em.getTransaction().begin();
        em.persist(accountWouldBeModified);
        em.persist(accountWouldBeDeleted);
        em.getTransaction().commit();

        UnitOfWork unitOfWork = new UnitOfWorkJpa(provider);
        accountWouldBeModified.deposit(new BigDecimal(1200));

        //Act
        unitOfWork.beginTransaction();
        unitOfWork.registerNew(transactionWouldNew);
        unitOfWork.registerModified(accountWouldBeModified);
        unitOfWork.registerDeleted(accountWouldBeDeleted);
        unitOfWork.commit();

        //Assert
        //Assert modified one verison number greater than zero
        Account modified = em.createNamedQuery("Account.findByAccountNo", Account.class)
                .setParameter("accountNo", accountWouldBeModified.getAccountNo())
                .getSingleResult();
        Assert.assertEquals(modified.getVersion().intValue() , 1);

        //Assert new one verison 0 and exists
        Transaction newAdded= em.createNamedQuery("Transaction.findByTransactionNo", Transaction.class)
                .setParameter("transactionNo", transactionWouldNew.getTransactionNo())
                .getSingleResult();
        Assert.assertEquals(newAdded.getVersion().intValue() , 0);

        //Assert deleted throws exception NoResultException
        try {
            Account deletedOne = em.createNamedQuery("Account.findByAccountNo", Account.class)
                    .setParameter("accountNo", accountWouldBeDeleted.getAccountNo())
                    .getSingleResult();
            Assert.fail();
        } catch (NoResultException e) {
            assert true;
        }
    }


    @Test
    public void testOptimisticLockException() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.transfer.commitFailed");

        //Arrange
        Account account = new Account.Builder("A1", new BigDecimal(100), "slmgnc").build();

        final EntityManager em = injector.getInstance(EntityManager.class);

        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        UnitOfWork unitOfWork = injector.getInstance(UnitOfWork.class);

        //Act
        unitOfWork.beginTransaction();
        account.deposit(new BigDecimal(1200));
        unitOfWork.registerModified(account);

        EntityManagerFactory factory = injector.getInstance(EntityManagerFactory.class);
        final EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        Account accountS2 = em2.createNamedQuery("Account.findByAccountNo", Account.class)
                .setParameter("accountNo", "A1")
                .getSingleResult();
        accountS2.deposit(new BigDecimal(400));
        em2.merge(accountS2);
        em2.getTransaction().commit();

        //Some object updated by other transaction shuld throw exception
        unitOfWork.commit();


    }
}
