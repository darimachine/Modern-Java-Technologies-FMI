package bg.sofia.uni.fmi.mjt.newsfeed.exceptions.apierrorcodes;

public class UnexpectedErrorException extends RuntimeException {
    public UnexpectedErrorException(String message) {
        super(message);
    }
}
