package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

public final class Car extends FuelTypeCars {


    public static final int pricePerSeat=5;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id,model,fuelType,numberOfSeats,pricePerWeek,pricePerDay,pricePerHour);

    }
    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        super.returnBack(rentalEnd);


    }
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        validateRentalPeriod(startOfRent, endOfRent);
        long hours;
        //long hours = java.time.Duration.between(startOfRent, endOfRent).toHours();
        double basePrice;
        hours=startOfRent.until(endOfRent,HOURS);
        if (hours >= 24 * 7) { // Calculate by week if rental is at least one week
            long weeks = (hours-1) / (24 * 7); // Round up to the next full week
            weeks++;
            basePrice = weeks * this.pricePerWeek;
        } else if (hours >= 24) { // Calculate by day if rental is at least one day
            long days = (hours + 23) / 24; // Round up to the next full day
            basePrice = days * this.pricePerDay;
        } else {
            basePrice = hours * this.pricePerHour; // Calculate by hours
        }

        // Add fuel type tax per day
        double fuelTax = this.fuelType.getCost();
        long rentalDays = (hours + 23) / 24; // Round up to the next full day for tax purposes

        // Add seat cost
        double seatCost = this.numberOfSeats * 5;

        return basePrice + (fuelTax * rentalDays) + seatCost;
    }
}
