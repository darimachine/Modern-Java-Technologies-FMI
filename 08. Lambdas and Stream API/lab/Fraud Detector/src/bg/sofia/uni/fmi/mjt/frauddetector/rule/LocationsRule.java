package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule implements Rule {

    private int threshold;
    private double weight;

    public LocationsRule(int threshold, double weight) {
        validateWeight(weight);
        this.threshold = threshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transactions list cannot be null or empty");
        }
        long differentLocations = transactions.stream()
            .map(Transaction::location)
            .distinct()
            .count();

        return threshold <= differentLocations;
    }

    @Override
    public double weight() {
        return weight;
    }

    private void validateWeight(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0.");
        }
    }
}
