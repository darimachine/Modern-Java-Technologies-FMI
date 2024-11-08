package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class VehicleAlreadyRentedException extends RuntimeException{
    public VehicleAlreadyRentedException()
    {
        super();
    }
    public VehicleAlreadyRentedException(String message){
        super(message);
    }
    public VehicleAlreadyRentedException(String message,Throwable cause)
    {
        super(message,cause);
    }
}
