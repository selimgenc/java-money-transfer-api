package com.revolut.bank.domain.transaction;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.shared.DomainEntity;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction extends DomainEntity {
    @NotNull(message = "{transaction.transactionNo.wrong}")
    private String transactionNo;
    @NotNull(message = "{transaction.from.wrong}")
    private Account from;
    @NotNull(message = "{transaction.to.wrong}")
    private Account to;
    @NotNull(message = "{transaction.amount.wrong}")
    private BigDecimal amount;
    @NotNull(message = "{transaction.time.wrong}")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    private String location;
    private String type;

    public Transaction(@NotNull(message = "{transaction.transactionNo.wrong}") String transactionNo, @NotNull(message = "{transaction.from.wrong}") Account from, @NotNull(message = "{transaction.to.wrong}") Account to, @NotNull(message = "{transaction.amount.wrong}") BigDecimal amount, @NotNull(message = "{transaction.time.wrong}") Date time) {
        this.transactionNo = transactionNo;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.time = time;
    }

    public Transaction() {
    }

    //region Getters and Setters

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    //endregion
}
