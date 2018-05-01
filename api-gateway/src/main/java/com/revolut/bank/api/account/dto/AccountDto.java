package com.revolut.bank.api.account.dto;

import com.revolut.bank.domain.account.Account;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountDto implements Serializable{
    private String customer;
    private String accountNumber;
    private BigDecimal credit;
    //todo other properties

    public static AccountDto createFromAccount(Account account){
        AccountDto dto = new AccountDto();
        dto.setAccountNumber(account.getAccountNo());
        dto.setCustomer(account.getCustomer());
        dto.setCredit(account.getCredit());
        return dto;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

}
