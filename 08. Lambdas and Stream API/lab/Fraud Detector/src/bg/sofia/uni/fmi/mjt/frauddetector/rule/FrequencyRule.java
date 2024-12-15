package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule implements Rule {

    private int transactionCountThreshold;
    private TemporalAmount timeWindow;
    private double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        validateWeight(weight);
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transactions list cannot be null or empty");
        }
        for (Transaction transaction : transactions) {
            LocalDateTime startTime = transaction.transactionDate();
            LocalDateTime endTime = startTime.plus(timeWindow);

            long countInWindow = transactions.stream()
                .filter(t -> (t.transactionDate().isEqual(startTime) || t.transactionDate().isAfter(startTime)) &&
                    (t.transactionDate().isEqual(endTime) || t.transactionDate().isBefore(endTime)))
                .count();

            if (countInWindow >= transactionCountThreshold) {
                return true;
            }
        }
        return false;
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
