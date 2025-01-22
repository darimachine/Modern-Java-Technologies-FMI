package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ParametersMissingException extends RuntimeException {
    public ParametersMissingException(String message) {
        super(message);
    }
}
