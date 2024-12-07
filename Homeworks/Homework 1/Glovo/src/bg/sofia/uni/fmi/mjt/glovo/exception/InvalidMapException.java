package bg.sofia.uni.fmi.mjt.glovo.exception;

/**
 * This exception is thrown when there is problem with the MapLayout.
 */
public class InvalidMapException extends RuntimeException {
    public InvalidMapException(String message) {
        super(message);
    }

    public InvalidMapException(String message, Throwable cause) {
        super(message, cause);
    }

}
