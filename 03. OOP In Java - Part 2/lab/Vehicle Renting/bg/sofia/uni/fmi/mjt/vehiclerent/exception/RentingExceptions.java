package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class RentingExceptions extends RuntimeException {
    public RentingExceptions(String message) {
        super(message);
    }
    public RentingExceptions(String message,Throwable cause)
    {
      super(message, cause);
    }
}
