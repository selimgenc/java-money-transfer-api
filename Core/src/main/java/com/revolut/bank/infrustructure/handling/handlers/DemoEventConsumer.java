package com.revolut.bank.infrustructure.handling.handlers;

import com.revolut.bank.application.events.Event;
import io.reactivex.functions.Consumer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoEventConsumer implements Consumer<Event>{
    private static final Logger logger = Logger.getLogger(DemoEventConsumer.class.getName());
    @Override
    public void accept(Event event) throws Exception {
        logger.log(Level.INFO, "Demo Event for: "+ event.toString());
    }
}
