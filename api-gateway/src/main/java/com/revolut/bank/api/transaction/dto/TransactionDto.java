package com.revolut.bank.api.transaction.dto;

import com.revolut.bank.domain.transaction.Transaction;

import javax.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDto implements Serializable {
    private String transactionNumber;
    private String from;
    private String to;
    private BigDecimal amount;
    private Date time;

    public static TransactionDto cretaFromEntity(Transaction transaction){
        TransactionDto dto = new TransactionDto();
        dto.setTransactionNumber(transaction.getTransactionNo());
        dto.setFrom(transaction.getFrom().getAccountNo());
        dto.setTo(transaction.getTo().getAccountNo());
        dto.setAmount(transaction.getAmount());
        dto.setTime(transaction.getTime());
        return dto;
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
}
