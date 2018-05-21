package com.revolut.bank.domain.account;

import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.DomainEntity;
import com.revolut.bank.domain.shared.Validate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Account.findAll",
                query = "Select a from Account a"),
        @NamedQuery(name = "Account.findByAccountNo",
                query = "Select a from Account a where a.accountNo = :accountNo")
})
public class Account extends DomainEntity {

    @NotNull(message = "{account.accountNo.wrong}")
    private String accountNo;
    @NotNull(message = "{account.credit.wrong}")
    private BigDecimal credit;
    @NotNull(message = "{account.customer.wrong}")
    private String customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    @Size(min = 1)
    private List<Movement> movements = Collections.emptyList();

    public Account(@NotNull(message = "{account.accountNo.wrong}") String accountNo,
                   @NotNull(message = "{account.credit.wrong}") BigDecimal credit,
                   @NotNull(message = "{account.customer.wrong}") String customer,
                   @NotNull(message = "{account.movements.wrong}") List<Movement> movements) {
        this.accountNo = accountNo;
        this.credit = credit;
        this.customer = customer;
        this.movements = movements;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo='" + accountNo + '\'' +
                ", credit=" + credit +
                ", customer='" + customer + '\'' +
                ", movements=" + movements +
                "} ";
    }

    public void withdraw(BigDecimal amount) throws DomainException {
        Validate.hasBalance(this, amount);
        addMovement(new Movement(amount, Movement.Type.WITHDRAW));
    }
    public void deposit(BigDecimal amount){
        addMovement(new Movement(amount, Movement.Type.DEPOSIT));
    }

    public String getAccountNo() {
        return accountNo;
    }
    public BigDecimal getCredit() {
        return credit;
    }
    public String getCustomer() {
        return customer;
    }
    public List<Movement> getMovements() {
        return Collections.unmodifiableList(movements);
    }

    private void addMovement(final Movement movement){
        movements.add(movement);
        this.credit = calculateCredit(this.credit, movement);
    }
    private static BigDecimal calculateCredit(BigDecimal credit, Movement movement) {
        switch (movement.getType()){
            case DEPOSIT:
            case INIT:
                credit = credit.add(movement.getAmount());
                break;
            case WITHDRAW:
                credit = credit.subtract(movement.getAmount());
                break;
        }
        return credit;
    }

    public static final class Builder{
        private String accountNo;
        private String customer;
        private BigDecimal credit = new BigDecimal(0);
        private List<Movement> movements = new LinkedList<>();

        public Builder(@NotNull(message = "{account.accountNo.wrong}") String accountNo,
                       @NotNull(message = "{account.credit.wrong}") BigDecimal credit,
                       @NotNull(message = "{account.customer.wrong}") String customer) {
            this.accountNo = accountNo;
            this.customer = customer;
            this.credit = credit;
            this.movements.add(new Movement(credit, Movement.Type.INIT));
        }

        public Builder addMovement(final Movement movement) {
            if( movement.getType() == Movement.Type.INIT){ //todo change with validator
                throw new IllegalArgumentException("Cannot add movement with init type, only by constructor");
            }

            this.movements.add(movement);
            return this;
        }

        public Account build(){
            this.credit = driveFromMovements(this.movements);
            return new Account(accountNo, credit, customer, movements);
        }

        private static BigDecimal driveFromMovements(final List<Movement> movements){
            BigDecimal credit = new BigDecimal(0);
            for (Movement movement : movements) {
                credit = calculateCredit(credit, movement);

            }
            return credit;
        }
    }

}
