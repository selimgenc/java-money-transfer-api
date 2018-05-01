package com.revolut.bank.application.events;

public interface Event<T extends Event> {
    /**
     * Returns the message type as a {@link Class} object. In this example the message type is
     * used to handle events by their type.
     *
     * @return the message type as a {@link Class}.
     */
    default Class<? extends Event> getType() {
        return getClass();
    }

}
