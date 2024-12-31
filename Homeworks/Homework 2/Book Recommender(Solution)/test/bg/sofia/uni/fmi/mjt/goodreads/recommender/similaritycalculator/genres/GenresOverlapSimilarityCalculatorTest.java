package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenresOverlapSimilarityCalculatorTest {

    private SimilarityCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new GenresOverlapSimilarityCalculator();
    }

    @Test
    void testCalculateSimilarityWithIdenticalGenres() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of("Fantasy", "History"), 4.5, 100, "url1");
        Book book2 = new Book("2", "Book 2", "Author 2", "Description 2",
            List.of("Fantasy", "History"), 4.8, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(1.0, similarity, 0.001, "Similarity between books with identical genres should be 1.0");
    }

    @Test
    void testCalculateSimilarityWithPartialOverlap() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of("Programming", "Data Science"), 4.5, 100, "url1");
        Book book2 = new Book("2", "Book 2", "Author 2", "Description 2",
            List.of("Programming", "Web Development"), 4.8, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.5, similarity, 0.001,
            "Similarity with 1 common genre and 2 genres in the smaller set should be 0.5");
    }

    @Test
    void testCalculateSimilarityWithNoOverlap() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of("Programming", "Data Science"), 4.5, 100, "url1");
        Book book2 = new Book("2", "Book 2", "Author 2", "Description 2",
            List.of("Web Development", "Cybersecurity"), 4.8, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001, "Similarity between books with no overlapping genres should be 0.0");
    }

    @Test
    void testCalculateSimilarityWithEmptyGenres() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of(), 4.5, 100, "url1");
        Book book2 = new Book("2", "Book 2", "Author 2", "Description 2",
            List.of("Programming"), 4.8, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001, "Similarity when one book has no genres should be 0.0");
    }

    @Test
    void testCalculateSimilarityWithBothEmptyGenres() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of(), 4.5, 100, "url1");
        Book book2 = new Book("2", "Book 2", "Author 2", "Description 2",
            List.of(), 4.8, 200, "url2");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001, "Similarity when both books have no genres should be 0.0");
    }

    @Test
    void testCalculateSimilarityWithNullBook() {
        Book book1 = new Book("1", "Book 1", "Author 1", "Description 1",
            List.of("Programming"), 4.5, 100, "url1");

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null),
            "Similarity calculation with a null book should throw NullPointerException");

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book1),
            "Similarity calculation with a null book should throw NullPointerException");
    }

}
