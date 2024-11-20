package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.EventComparator;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {

    private Map<Class<? extends Event<?>>, Set<Subscriber<?>>> subscriptions;
    private Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;
    public EventBusImpl() {
        this.subscriptions = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        Set<Subscriber<?>> subscribers = subscriptions.get(eventType);
        if (subscribers == null) {
            subscribers = new HashSet<>();
            subscriptions.put(eventType, subscribers);
        }
        subscribers.add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        Set<Subscriber<?>> subscribers = subscriptions.get(eventType);
        if (subscribers == null || !subscribers.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber not found for event type " + eventType.getName());
        }

        if (subscribers.isEmpty()) {
            subscriptions.remove(eventType);
        }

    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        Set<Subscriber<?>> subscribers = subscriptions.get(event.getClass());
        if (subscribers != null) {
            for (Subscriber<?> subscriber : subscribers) {

                Subscriber<T> typedSubscriber = (Subscriber<T>) subscriber;
                typedSubscriber.onEvent(event);
            }
        }

        List<Event<?>> logs = eventLogs.get(event.getClass());
        if (logs == null) {
            logs = new ArrayList<>();
            eventLogs.put((Class<? extends Event<?>>) event.getClass(), logs);
        }
        logs.add(event);
    }

    @Override
    public void clear() {
        subscriptions.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Event type, from, and to timestamps cannot be null");
        }

        List<Event<?>> logs = eventLogs.get(eventType);
        if (logs == null) {
            return Collections.emptyList();
        }

        List<Event<?>> filteredLogs = new ArrayList<>();
        for (Event<?> event : logs) {
            Instant timestamp = event.getTimestamp();
            if (!timestamp.isBefore(from) && timestamp.isBefore(to)) {
                filteredLogs.add(event);
            }
        }

        filteredLogs.sort(new EventComparator().reversed());

        return Collections.unmodifiableList(filteredLogs);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        Set<Subscriber<?>> subscribers = subscriptions.get(eventType);
        if (subscribers == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(subscribers);
    }
}
