package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public sealed abstract class Vehicle permits Bicycle, FuelTypeCars {

    private String id;
    private String model;
    protected LocalDateTime startRentTime;
    private LocalDateTime rentalEnd;
    boolean isRented=false;
    Driver currentDriver=null;
    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }
    public LocalDateTime getStartRentTime()
    {
        return startRentTime;
    }
    public boolean isRented()
    {
        return isRented;
    }
    public Driver getDriver()
    {
        return currentDriver;
    }
    protected void validateRentalPeriod(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (endOfRent.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException("End date cannot be before the start date");
        }
    }
    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     * @param driver the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {
        if (driver == null || startRentTime == null) {
            throw new IllegalArgumentException("Driver and startRentTime must not be null");
        }
        if(driver==currentDriver || isRented)
        {
            throw new VehicleAlreadyRentedException("Vehicle already rented");
        }
        this.currentDriver = driver;
        this.startRentTime = startRentTime;
        this.isRented = true;
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd == null) {
            throw new IllegalArgumentException("Rental end time must not be null");
        }
        if (!isRented) {
            throw new VehicleNotRentedException("Vehicle is not currently rented");
        }
        if (rentalEnd.isBefore(startRentTime)) {
            throw new InvalidRentingPeriodException("Rental end time cannot be before the start rent time");
        }
        isRented = false;
        currentDriver = null;
        startRentTime = null;
//
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     * the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

}
