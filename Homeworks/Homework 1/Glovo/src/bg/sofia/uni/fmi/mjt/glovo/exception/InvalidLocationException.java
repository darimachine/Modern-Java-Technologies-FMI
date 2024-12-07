package bg.sofia.uni.fmi.mjt.glovo.exception;

/**
 * This exception is thrown when an invalid location is provided.
 */
public class InvalidLocationException extends RuntimeException {
    public InvalidLocationException(String message) {
        super(message);
    }

    public InvalidLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
