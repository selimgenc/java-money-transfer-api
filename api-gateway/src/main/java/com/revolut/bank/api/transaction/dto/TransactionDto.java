package com.revolut.bank.api.transaction.dto;

import com.revolut.bank.domain.transaction.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDto implements Serializable {
    private String transactionNumber;
    private String from;
    private String to;
    private BigDecimal amount;
    private Date time;
    private String user;
    private String location;

    public static TransactionDto buildFromEntity(Transaction transaction){
        TransactionDto dto = new TransactionDto();
        dto.setTransactionNumber(transaction.getTransactionNo());
        dto.setFrom(transaction.getFrom());
        dto.setTo(transaction.getTo());
        dto.setAmount(transaction.getAmount());
        dto.setTime(transaction.getTime());
        dto.setUser(transaction.getUser());
        dto.setLocation(transaction.getLocation());
        return dto;
    }

    public TransactionDto(String from, String to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public TransactionDto() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
