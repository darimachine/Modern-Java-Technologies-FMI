package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class SourceDoesNotExistException extends RuntimeException {
    public SourceDoesNotExistException(String message) {
        super(message);
    }
}
