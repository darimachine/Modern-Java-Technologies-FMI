package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (similarityCalculatorMap == null || similarityCalculatorMap.isEmpty()) {
            throw new IllegalArgumentException("SimilarityCalculatorMap is null or empty");
        }
        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Books cannot be null");
        }
        double weightedSum = 0.0;

        for (var entry : similarityCalculatorMap.entrySet()) {
            SimilarityCalculator calculator = entry.getKey();
            double weight = entry.getValue();
            double similarity = calculator.calculateSimilarity(first, second);
            weightedSum += similarity * weight;
        }
        return weightedSum;
    }

}
