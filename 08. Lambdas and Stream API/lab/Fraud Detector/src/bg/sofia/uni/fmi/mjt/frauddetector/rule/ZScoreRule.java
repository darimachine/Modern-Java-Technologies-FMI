package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {


    double zScoreThreshold;
    double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        validateWeight(weight);
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transactions list cannot be null or empty");
        }
        long countTransactions = transactions.size();
        double middleValue = calculateMiddleValue(transactions);
        double dispersion = 0;
        for (int i = 0; i < countTransactions; i++) {
            dispersion += Math.pow(transactions.get(i).transactionAmount() - middleValue, 2);
        }
        dispersion /= countTransactions;
        double standardDeviation = Math.sqrt(dispersion);

        return transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .anyMatch(amount -> calculateZscore(amount, middleValue, standardDeviation) >= zScoreThreshold);
    }

    @Override
    public double weight() {
        return weight;
    }

    double calculateZscore(double amount, double mean, double standardDeviation) {
        if (standardDeviation == 0) {
            return 0;
        }
        return Math.abs(amount - mean) / standardDeviation;
    }

    double calculateMiddleValue(List<Transaction> transactions) {
        return transactions.stream()
        .mapToDouble(Transaction::transactionAmount)
        .average()
        .orElse(0.0);
    }

    private void validateWeight(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0.");
        }
    }
}
