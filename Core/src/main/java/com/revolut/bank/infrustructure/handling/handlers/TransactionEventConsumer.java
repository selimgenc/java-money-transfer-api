package com.revolut.bank.infrustructure.handling.handlers;


import com.revolut.bank.application.events.Event;
import com.revolut.bank.application.events.transaction.TransactionCreatedEvent;
import io.reactivex.functions.Consumer;

import java.util.logging.Logger;

public class TransactionEventConsumer implements Consumer<Event> {
    private static final Logger logger = Logger.getLogger(TransactionEventConsumer.class.getName());

    @Override
    public void accept(Event event) throws Exception {
        if(event instanceof TransactionCreatedEvent){
            TransactionCreatedEvent transactionCreatedEvent = (TransactionCreatedEvent) event;
            logger.info("Could send e-mail to stakeholders: " + transactionCreatedEvent.getTransaction().getTransactionNo());
        }
    }
}
