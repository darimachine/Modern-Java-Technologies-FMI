package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.WEEKS;

public final class Bicycle extends Vehicle{

    private double pricePerDay;
    private double pricePerHour;
    public Bicycle(String id, String model, double pricePerDay, double pricePerHour){
        super(id,model);
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
    }
    @Override
    public void rent(Driver driver, LocalDateTime startRentTime) {

        super.rent(driver,startRentTime);
    }
    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {

        if(startRentTime.plusDays(6).plusHours(23).plusMinutes(59).plusSeconds(59).isBefore(rentalEnd))
        {
            throw new InvalidRentingPeriodException("Renting a bike is more than 1 week");
        }
        super.returnBack(rentalEnd);


    }
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        validateRentalPeriod(startOfRent, endOfRent);

        long hours = java.time.Duration.between(startOfRent, endOfRent).toHours();
        hours=startOfRent.until(endOfRent,HOURS);
        if (hours > 24 * 7) { // Ensure max rental is not more than a week
            throw new InvalidRentingPeriodException("Bicycles cannot be rented for more than a week");
        }

        double dailyRate = this.pricePerDay;
        double hourlyRate = this.pricePerHour;

        if (hours >= 24) { // Calculate by days if rental is at least 1 day
            long days = (hours + 23) / 24; // Round up to the next full day
            return days * dailyRate;
        } else {
            return hours * hourlyRate; // Calculate by hours
        }
    }
}
