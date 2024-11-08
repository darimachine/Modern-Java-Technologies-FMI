package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public abstract sealed class FuelTypeCars extends Vehicle permits Car,Caravan{

    FuelType fuelType;
    int numberOfSeats;
    double pricePerWeek;
    double pricePerDay;
    double pricePerHour;

    public static final int pricePerSeat=5;
    public FuelTypeCars(String id, String model,FuelType fuelType,int numberOfSeats,double pricePerWeek,double pricePerDay,double pricePerHour) {
        super(id, model);
        this.fuelType=fuelType;
        this.numberOfSeats=numberOfSeats;
        this.pricePerWeek=pricePerWeek;
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;

    }
}
