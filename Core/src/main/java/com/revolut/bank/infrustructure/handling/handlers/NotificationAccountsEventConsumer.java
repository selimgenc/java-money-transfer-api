package com.revolut.bank.infrustructure.handling.handlers;

import com.revolut.bank.application.events.Event;
import com.revolut.bank.application.events.account.AccountCreatedEvent;
import io.reactivex.functions.Consumer;

import java.util.logging.Logger;

public class NotificationAccountsEventConsumer implements Consumer<Event> {
    private static final Logger logger = Logger.getLogger(NotificationAccountsEventConsumer.class.getName());

    @Override
    public void accept(Event event) throws Exception {
        if(event instanceof AccountCreatedEvent){
            AccountCreatedEvent accountCreatedEvent = (AccountCreatedEvent) event;
            logger.info("Send Notfication to customer: " + accountCreatedEvent.getAccount().getCustomer());
        }
    }
}
