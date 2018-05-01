package com.revolut.bank.domain.account;

import com.revolut.bank.domain.shared.DomainEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Account extends DomainEntity {

    @NotNull(message = "{account.accountNo.wrong}")
    private String accountNo;
    @NotNull(message = "{account.credit.wrong}")
    private BigDecimal credit;
    @NotNull(message = "{account.customer.wrong}")
    private String customer;

    public Account(@NotNull(message = "{account.accountNo.wrong}") String accountNo, @NotNull(message = "{account.credit.wrong}") BigDecimal credit, @NotNull(message = "{account.customer.wrong}") String customer) {
        this.accountNo = accountNo;
        this.credit = credit;
        this.customer = customer;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo='" + accountNo + '\'' +
                ", credit=" + credit +
                ", customer='" + customer + '\'' +
                "} " + super.toString();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

}
