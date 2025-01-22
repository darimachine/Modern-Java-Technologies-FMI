package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class RateLimitedException extends RuntimeException {
    public RateLimitedException(String message) {
        super(message);
    }
}
