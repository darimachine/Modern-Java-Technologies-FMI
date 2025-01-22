package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ApiKeyInvalidException extends RuntimeException {
    public ApiKeyInvalidException(String message) {
        super(message);
    }
}
