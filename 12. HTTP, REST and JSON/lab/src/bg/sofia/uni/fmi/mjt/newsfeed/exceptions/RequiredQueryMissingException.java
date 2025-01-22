package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class RequiredQueryMissingException extends RuntimeException {
    public RequiredQueryMissingException(String message) {
        super(message);
    }

}
