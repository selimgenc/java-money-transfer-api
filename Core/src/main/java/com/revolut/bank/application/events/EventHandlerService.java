package com.revolut.bank.application.events;

public interface EventHandlerService {
    <E extends Event> void send(E event);
}
