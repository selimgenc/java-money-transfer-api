package com.revolut.bank.application;

import com.revolut.bank.application.events.EventHandlerService;
import com.revolut.bank.application.internal.TransactionServiceImpl;
import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.AccountRepository;
import com.revolut.bank.domain.handling.DomainException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionServiceTest extends ApplicationTestBase{

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UnitOfWork mockUnitOfWork;

    @Mock
    private EventHandlerService eventHandlerService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createTransfer_Successful() throws DomainException {
        Account account1 = new Account.Builder("account1", new BigDecimal(100), "test").build();
        Account account2 = new Account.Builder("account2", new BigDecimal(100), "test").build();
        Mockito.when(accountRepository.getByNo("account1")).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.getByNo("account2")).thenReturn(Optional.of(account2));
//        Mockito.when(unitOfWorkFactory.get()).thenReturn(mockUnitOfWork);

        BigDecimal account1StartCredit = account1.getCredit();
        BigDecimal account2StartCredit = account2.getCredit();

        String accountNoFrom = account1.getAccountNo();
        String accountNoTo = account2.getAccountNo();
        int amaount = 10;
        String transferID = transactionService.transfer(accountNoFrom, accountNoTo, new BigDecimal(amaount), "slm");

        //Assert
        Assert.assertTrue(!transferID.isEmpty());
        Assert.assertEquals(account1StartCredit.subtract(new BigDecimal(amaount)), account1.getCredit());
        Assert.assertEquals(account2StartCredit.add(new BigDecimal(amaount)), account2.getCredit());

    }

    @Test
    public void test_transfer_account_not_found() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.notfound");

        transactionService.transfer("someAccountNumber", "someAccountNumber2", new BigDecimal(10), "");
    }

    @Test
    public void test_transfer_With_NotEnough_Balance_to_Transfer() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("account.transfer.notenoughbalance");

        Account account1 = new Account.Builder("account1", new BigDecimal(10), "test").build();
        Account account2 = new Account.Builder("account2", new BigDecimal(10), "test").build();
        Mockito.when(accountRepository.getByNo("account1")).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.getByNo("account2")).thenReturn(Optional.of(account2));

        transactionService.transfer(account1.getAccountNo(), account2.getAccountNo(), new BigDecimal(50), "");

    }
    @Test
    public void test_transfer_cannot_transfer_to_itself() throws DomainException {
        exception.expect(DomainException.class);
        exception.expectMessage("transfer.samAccount");

        Account account1 = new Account.Builder("account1", new BigDecimal(10), "test").build();
        Mockito.when(accountRepository.getByNo("account1")).thenReturn(Optional.of(account1));

        transactionService.transfer(account1.getAccountNo(), account1.getAccountNo(), new BigDecimal(10), "");

    }


}
