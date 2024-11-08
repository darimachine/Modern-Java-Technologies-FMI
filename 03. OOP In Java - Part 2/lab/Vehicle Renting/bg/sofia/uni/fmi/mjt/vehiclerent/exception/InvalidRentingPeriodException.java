package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class InvalidRentingPeriodException extends RentingExceptions{

    public InvalidRentingPeriodException()
    {
        super("Invalid Renting Period Exception");
    }
    public InvalidRentingPeriodException(String message)
    {
        super(message);
    }

}
