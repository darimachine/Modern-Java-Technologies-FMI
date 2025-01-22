package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ApiKeyExhaustedException extends RuntimeException {
    public ApiKeyExhaustedException(String message) {
        super(message);
    }
}
