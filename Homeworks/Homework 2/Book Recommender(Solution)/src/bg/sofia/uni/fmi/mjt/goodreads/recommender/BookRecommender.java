package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {

    private final Set<Book> books;
    private final SimilarityCalculator calculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null || calculator == null) {
            throw new IllegalArgumentException("Books set and calculator cannot be null");
        }
        this.books = initialBooks;
        this.calculator = calculator;
    }

    // THIS METHOD CAN BE OPTIMIZED USING THE METHOD DOWN WHICH IS COMMENTED BECAUSE HERE IT CALCULATES SIMILARITIES MULTIPLE TIMES
    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        validateInputs(origin, maxN);
        Comparator<Book> comparator = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                double similarityBook1 = calculator.calculateSimilarity(origin, o1);
                double similarityBook2 = calculator.calculateSimilarity(origin, o2);
                int comparison = Double.compare(similarityBook2, similarityBook1);
                if (comparison == 0) {
                    return o1.title().compareTo(o2.title());
                }
                return comparison;
            }
        };
        SortedMap<Book, Double> recommendedBooks = books.stream()
            .filter(book -> !book.equals(origin))
            .sorted(comparator)
            .limit(maxN)
            .collect(Collectors.toMap(
                book -> book,
                book -> calculator.calculateSimilarity(origin, book),
                (e1, e2) -> e1,
                () -> new TreeMap<>(comparator)
            ));
        // CAN BE MADE WITH SECOND MAP BUT WITH 1 MAP THIS IS THE WAY WE NEED TO FIRST SORT IT AFTER THAT LIMIT,
        // BECAUSE LIMIT IS LAZY OPERATION
        return recommendedBooks;
    }

    private void validateInputs(Book origin, int maxN) {
        if (maxN <= 0) {
            throw new IllegalArgumentException("maxN must be greater than 0");
        }
        if (origin == null) {
            throw new IllegalArgumentException("book is null");
        }
    }

//    private Map<Book, Double> calculateSimilarities(Book origin) {
//        return books.stream()
//            .filter(book -> !book.equals(origin))
//            .collect(Collectors.toMap(
//                book -> book,
//                book -> calculator.calculateSimilarity(origin, book)
//            ));
//    }

}
