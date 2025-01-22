package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ApiKeyMissingException extends RuntimeException {
    public ApiKeyMissingException(String message) {
        super(message);
    }
}
