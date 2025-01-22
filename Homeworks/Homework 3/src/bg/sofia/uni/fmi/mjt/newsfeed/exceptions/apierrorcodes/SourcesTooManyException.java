package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class SourcesTooManyException extends RuntimeException {
    public SourcesTooManyException(String message) {
        super(message);
    }
}
