package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ApiKeyDisabledException extends RuntimeException {
    public ApiKeyDisabledException(String message) {
        super(message);
    }
}
