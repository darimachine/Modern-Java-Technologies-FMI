package bg.sofia.uni.fmi.mjt.eventbus;

import java.time.Instant;
import java.util.Collection;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

public interface EventBus {
         /**
         * Subscribes the given subscriber to the given event type.
         *
         * @param eventType  the type of event to subscribe to
         * @param subscriber the subscriber to subscribe
         * @throws IllegalArgumentException if the event type is null
         * @throws IllegalArgumentException if the subscriber is null
         */ <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber);

        /**
         * Unsubscribes the given subscriber from the given event type.
         *
         * @param eventType  the type of event to unsubscribe from
         * @param subscriber the subscriber to unsubscribe
         * @throws IllegalArgumentException     if the event type is null
         * @throws IllegalArgumentException     if the subscriber is null
         * @throws MissingSubscriptionException if the subscriber is not subscribed to the event type
         */ <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
                throws MissingSubscriptionException;

        /**
         * Publishes the given event to all subscribers of the event type.
         *
         * @param event the event to publish
         * @throws IllegalArgumentException if the event is null
         */ <T extends Event<?>> void publish(T event);

        /**
         * Clears all subscribers and event logs.
         */ void clear();

        /**
         * Returns all events of the given event type that occurred between the given timestamps. If
         * {@code from} and {@code to} are equal the returned collection is empty.
         * <p> {@code from} - inclusive, {@code to} - exclusive. </p>
         *
         * @param eventType the type of event to get
         * @param from      the start timestamp (inclusive)
         * @param to        the end timestamp (exclusive)
         * @return an unmodifiable collection of events of the given event type that occurred between
         * the given timestamps
         * @throws IllegalArgumentException if the event type is null
         * @throws IllegalArgumentException if the start timestamp is null
         * @throws IllegalArgumentException if the end timestamp is null
         */ Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                    Instant to);

        /**
         * Returns all subscribers for the given event type in an unmodifiable collection. If there are
         * no subscribers for the event type, the method returns an empty unmodifiable collection.
         *
         * @param eventType the type of event to get subscribers for
         * @return an unmodifiable collection of subscribers for the given event type
         * @throws IllegalArgumentException if the event type is null
         */ <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType);

}