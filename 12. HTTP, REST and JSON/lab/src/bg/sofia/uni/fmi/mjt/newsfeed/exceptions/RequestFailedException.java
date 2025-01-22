package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String message) {
        super(message);
    }

    public RequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
