package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public interface Event<T extends  Payload<?>> {
    /**
     * @return the time when the event was created.
     */
    Instant getTimestamp();

    /**
     * @return the priority of the event. Lower number denotes higher priority.
     */
    int getPriority();

    /**
     * @return the source of the event.
     */
    String getSource();

    /**
     * @return the payload of the event.
     */
    T getPayload();
}
