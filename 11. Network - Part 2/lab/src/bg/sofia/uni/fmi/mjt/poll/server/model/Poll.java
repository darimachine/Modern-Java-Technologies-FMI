package bg.sofia.uni.fmi.mjt.poll.server.model;

import java.util.Map;

public record Poll(String question, Map<String, Integer> options) {
    public Poll {
        if (question == null || options == null) {
            throw new IllegalArgumentException("Question and options cannot be null");
        }
        if (options.size() < 2) {
            throw new IllegalArgumentException("Poll must have at least 2 options");
        }
        if (options.values().stream().anyMatch(v -> v < 0)) {
            throw new IllegalArgumentException("Votes cannot be negative");
        }
    }
}
