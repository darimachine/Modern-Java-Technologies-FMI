package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.time.Instant;
import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        Integer order = Integer.compare(o2.getPriority(), o1.getPriority());
        if (order != 0) {
            return order;
        }
        Instant timestamp1 = o1.getTimestamp();
        Instant timestamp2 = o2.getTimestamp();
        return timestamp1.compareTo(timestamp2);

    }
}
