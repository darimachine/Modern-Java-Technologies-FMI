package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;

public final class Caravan extends FuelTypeCars{
    int numberOfBeds;
    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id,model,fuelType,numberOfSeats,pricePerWeek,pricePerDay,pricePerHour);
        this.numberOfBeds=numberOfBeds;
    }
    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException{
        if (rentalEnd.isBefore(super.startRentTime.plusDays(1))) {
            throw new InvalidRentingPeriodException("Caravans must be rented for at least a day");
        }
        super.returnBack(rentalEnd);



    }
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        validateRentalPeriod(startOfRent, endOfRent);

        long hours = java.time.Duration.between(startOfRent, endOfRent).toHours();
        if (hours < 24) { // Ensure minimum rental is 1 day
            throw new InvalidRentingPeriodException("Caravans must be rented for at least a day");
        }

        double basePrice;
        if (hours >= 24 * 7) { // Calculate by week if rental is at least one week
            long weeks = (hours + 167) / (24 * 7); // Round up to the next full week
            basePrice = weeks * this.pricePerWeek;
        } else { // Calculate by day if rental is less than a week
            long days = (hours + 23) / 24; // Round up to the next full day
            basePrice = days * this.pricePerDay;
        }

        // Add fuel type tax per day
        double fuelTax = this.fuelType.getCost();
        long rentalDays = (hours + 23) / 24; // Round up to the next full day for tax purposes

        // Add seat and bed costs
        double seatCost = this.numberOfSeats * 5;
        double bedCost = this.numberOfBeds * 10;

        return basePrice + (fuelTax * rentalDays) + seatCost + bedCost;
    }
}
