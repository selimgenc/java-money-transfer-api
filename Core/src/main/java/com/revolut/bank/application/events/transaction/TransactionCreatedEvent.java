package com.revolut.bank.application.events.transaction;

import com.revolut.bank.application.events.Event;
import com.revolut.bank.domain.transaction.Transaction;

import java.util.Date;
import java.util.Objects;

public class TransactionCreatedEvent implements Event<TransactionCreatedEvent> {
    private Transaction transaction;
    private Date eventTime;

    public TransactionCreatedEvent(Transaction transaction, Date eventTime) {
        this.transaction = transaction;
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "TransactionCreatedEvent{" +
                "transaction=" + transaction +
                ", eventTime=" + eventTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCreatedEvent that = (TransactionCreatedEvent) o;
        return Objects.equals(transaction, that.transaction) &&
                Objects.equals(eventTime, that.eventTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(transaction, eventTime);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}
