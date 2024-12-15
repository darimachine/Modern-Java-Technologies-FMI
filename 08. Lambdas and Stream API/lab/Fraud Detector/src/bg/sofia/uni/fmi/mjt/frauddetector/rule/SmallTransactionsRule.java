package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {

    private int countThreshold;
    private double weight;
    private double amountThreshold;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        validateWeight(weight);
        this.countThreshold = countThreshold;
        this.amountThreshold = amountThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transactions list cannot be null or empty");
        }
        long countNumberOfTransaction = transactions.stream()
            .filter(e -> e.transactionAmount() < amountThreshold)
            .count();
        return countNumberOfTransaction >= countThreshold;
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
