package com.revolut.bank.domain;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.Movement;
import com.revolut.bank.domain.handling.DomainException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTest {
    @Test
    public void testBuilder() {
        String accountNo = "Acc-1";
        BigDecimal credit = BigDecimal.valueOf(100);
        String customer = "Cus-1";
        Account account = new Account.Builder(accountNo, credit, customer)
                .build();

        //assert
        assertEquals(accountNo, account.getAccountNo());
        assertEquals(credit, account.getCredit());
        assertEquals(customer, account.getCustomer());

        Movement movementInit = new Movement(credit, Movement.Type.INIT);
        assertTrue(movementInit.equals(account.getMovements().get(0)));

    }

    @Test
    public void testBuilderWithMovements() {
        String accountNo = "Acc-1";
        BigDecimal credit = BigDecimal.valueOf(100);
        String customer = "Cus-1";
        Account account = new Account.Builder(accountNo, credit, customer)
                .addMovement(new Movement(BigDecimal.valueOf(20), Movement.Type.DEPOSIT))
                .addMovement(new Movement(BigDecimal.valueOf(40), Movement.Type.WITHDRAW))
                .build();

        //assert
        assertEquals(BigDecimal.valueOf(100+20-40), account.getCredit());
        assertEquals(3, account.getMovements().size());
        assertEquals(Movement.Type.INIT, account.getMovements().get(0).getType());
        assertEquals(Movement.Type.DEPOSIT, account.getMovements().get(1).getType());
        assertEquals(Movement.Type.WITHDRAW, account.getMovements().get(2).getType());
    }



    @Test
    public void testDepositWithMovements() {
        String accountNo = "Acc-1";
        BigDecimal credit = BigDecimal.valueOf(100);
        String customer = "Cus-1";
        Account account = new Account.Builder(accountNo, credit, customer)
                .build();

        BigDecimal depositAmount = BigDecimal.valueOf(50);
        Movement movementDeposit = new Movement(depositAmount, Movement.Type.DEPOSIT);
        account.deposit(depositAmount);
        //Assert
        assertEquals(credit.add(depositAmount), account.getCredit());
        assertTrue(movementDeposit.equals(account.getMovements().get(1)));

    }

    @Test
    public void testWithdrawWithMovements() throws DomainException {
        String accountNo = "Acc-1";
        BigDecimal credit = BigDecimal.valueOf(100);
        String customer = "Cus-1";
        Account account = new Account.Builder(accountNo, credit, customer)
                .build();

        BigDecimal withdrawAmount= BigDecimal.valueOf(50);
        Movement movementWithdraw= new Movement(withdrawAmount, Movement.Type.WITHDRAW);
        account.withdraw(withdrawAmount);

        //Assert
        assertEquals(credit.subtract(withdrawAmount), account.getCredit());
        assertTrue(movementWithdraw.equals(account.getMovements().get(1)));

    }
}
