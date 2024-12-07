package bg.sofia.uni.fmi.mjt.glovo.exception;

public class NoAvailableDeliveryGuyException extends RuntimeException {
    public NoAvailableDeliveryGuyException(String message) {
        super(message);
    }

    public NoAvailableDeliveryGuyException(String message, Throwable cause) {
        super(message, cause);
    }
}
