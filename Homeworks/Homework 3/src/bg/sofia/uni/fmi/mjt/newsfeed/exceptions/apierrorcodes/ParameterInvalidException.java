package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class ParameterInvalidException extends RuntimeException {
    public ParameterInvalidException(String message) {
        super(message);
    }
}
