package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public interface Subscriber<T extends Event<?>> {
    /**
     * This method will be called by the EventBus when a new event for the type this subscriber is
     * subscribed to is published.
     *
     * @param event the event that was published
     * @throws IllegalArgumentException if the event is null
     */
    void onEvent(T event);
}
