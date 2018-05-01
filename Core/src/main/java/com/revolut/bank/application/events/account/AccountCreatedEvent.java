package com.revolut.bank.application.events.account;

import com.revolut.bank.application.events.Event;
import com.revolut.bank.domain.account.Account;

import java.util.Date;
import java.util.Objects;

public class AccountCreatedEvent implements Event<AccountCreatedEvent> {

    private Account account;
    private Date eventTime;
    private String eventLocation;

    public AccountCreatedEvent(Account account, Date eventTime, String eventLocation) {
        this.account = account;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
    }

    public AccountCreatedEvent(Account account, Date eventTime) {
        this.account = account;
        this.eventTime = eventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCreatedEvent that = (AccountCreatedEvent) o;
        return Objects.equals(account, that.account) &&
                Objects.equals(eventTime, that.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, eventTime);
    }

    @Override
    public String toString() {
        return "AccountCreatedEvent{" +
                "account=" + account +
                ", eventTime=" + eventTime +
                ", eventLocation='" + eventLocation + '\'' +
                '}';
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
