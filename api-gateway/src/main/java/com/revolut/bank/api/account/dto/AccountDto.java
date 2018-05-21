package com.revolut.bank.api.account.dto;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.account.Movement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class AccountDto implements Serializable{
    private String customer;
    private String accountNumber;
    private BigDecimal credit;
    private List<MovementDto> movements = new LinkedList<>();

    public static AccountDto buildFromAccount(Account account){
        AccountDto dto = new AccountDto();
        dto.setAccountNumber(account.getAccountNo());
        dto.setCustomer(account.getCustomer());
        dto.setCredit(account.getCredit());
        return dto;
    }

    public static AccountDto buildFromAccountWithMovements(Account account){
        AccountDto dto = buildFromAccount(account);
        for (Movement movement : account.getMovements()) {
            dto.movements.add(MovementDto.buildFromEntity(movement));
        }
        return dto;
    }

    public AccountDto(String customer, BigDecimal credit) {
        this.customer = customer;
        this.credit = credit;
    }

    public AccountDto() {
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

    public List<MovementDto> getMovements() {
        return movements;
    }

}
