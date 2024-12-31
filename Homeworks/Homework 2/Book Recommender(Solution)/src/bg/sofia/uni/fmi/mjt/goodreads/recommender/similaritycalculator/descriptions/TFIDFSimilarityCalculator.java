package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private Set<Book> books;
    private TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("First or Second Book is Null");
        }
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        Map<String, Double> tf = computeTF(book);
        Map<String, Double> idf = computeIDF(book);
        List<String> tokens = tokenizer.tokenize(book.description());

        Map<String, Double> tfIdf = new HashMap<>();
        for (String word : tokens) {
            double tfScore = tf.getOrDefault(word, 0.00);
            double idfScore = idf.getOrDefault(word, 0.0);
            tfIdf.put(word, tfScore * idfScore);
        }

        return tfIdf;
    }

    public Map<String, Double> computeTF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book is null");
        }
        List<String> words = tokenizer.tokenize(book.description());
        int totalWords = words.size();
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        Map<String, Double> formatted = new HashMap<>();
        for (var entry : wordCounts.entrySet()) {
            String word = entry.getKey();
            Integer occurience = entry.getValue();
            formatted.put(word, (double) occurience / totalWords);
        }
        return formatted;
    }

    public Map<String, Double> computeIDF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book is null");
        }

        Map<String, Set<Book>> wordCountByBooks = new HashMap<>();
        for (var b : books) {
            List<String> tokenizedWords = tokenizer.tokenize(b.description());
            for (String word : tokenizedWords) {
                wordCountByBooks.putIfAbsent(word, new HashSet<>());
                wordCountByBooks.get(word).add(b);
            }
        }
        Map<String, Double> idfWords = tokenizer.tokenize(book.description()).stream()
            .distinct()
            .collect(Collectors.toMap(
                word -> word,
                word -> Math.log10((double) books.size() / wordCountByBooks.get(word).size())
            ));
        return idfWords;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}
