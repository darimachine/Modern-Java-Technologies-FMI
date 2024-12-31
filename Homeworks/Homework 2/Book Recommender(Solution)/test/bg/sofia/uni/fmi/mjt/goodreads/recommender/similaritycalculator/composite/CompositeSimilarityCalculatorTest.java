package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeSimilarityCalculatorTest {

    private SimilarityCalculator genreCalculator;
    private SimilarityCalculator descriptionCalculator;
    private CompositeSimilarityCalculator compositeCalculator;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        genreCalculator = mock();
        descriptionCalculator = mock();
        compositeCalculator = mock();
        book1 = new Book("1", "Book 1", "Author 1", "Java programming",
            List.of("Programming"), 4.5, 100, "url1");
        book2 = new Book("2", "Book 2", "Author 2", "Advanced Java",
            List.of("Programming", "Web Development"), 4.8, 200, "url2");

        when(genreCalculator.calculateSimilarity(book1, book2)).thenReturn(0.8);
        when(descriptionCalculator.calculateSimilarity(book1, book2)).thenReturn(0.6);
        compositeCalculator = new CompositeSimilarityCalculator(
            Map.of(
                genreCalculator, 0.7,
                descriptionCalculator, 0.3
            )
        );
    }

    @Test
    void testCalculateSimilarityWithNullFirstBook() {
        assertThrows(IllegalArgumentException.class,
            () -> compositeCalculator.calculateSimilarity(null, book2),
            "calculateSimilarity should throw an exception when the first book is null");
    }

    @Test
    void testCalculateSimilarityWithNullSecondBook() {
        assertThrows(IllegalArgumentException.class,
            () -> compositeCalculator.calculateSimilarity(book1, null),
            "calculateSimilarity should throw an exception when the second book is null");
    }

    @Test
    void testConstructorWithNullMap() {
        assertThrows(IllegalArgumentException.class,
            () -> new CompositeSimilarityCalculator(null),
            "Constructor should throw an exception when the map is null");
    }

    @Test
    void testConstructorWithEmptyMap() {
        assertThrows(IllegalArgumentException.class,
            () -> new CompositeSimilarityCalculator(Map.of()),
            "Constructor should throw an exception when the map is empty");
    }

    @Test
    void testCalculateSimilarityWithValidInput() {
        double similarity = compositeCalculator.calculateSimilarity(book1, book2);

        // 0.8 * 0.7 + 0.6 * 0.3 = 0.74
        assertEquals(0.74, similarity, 0.001,
            "The weighted similarity should be correctly calculated");
    }

    @Test
    void testCalculateSimilarityWithZeroWeights() {
        CompositeSimilarityCalculator zeroWeightCalculator = new CompositeSimilarityCalculator(
            Map.of(
                genreCalculator, 0.0,
                descriptionCalculator, 0.0
            )
        );

        double similarity = zeroWeightCalculator.calculateSimilarity(book1, book2);

        assertEquals(0.0, similarity, 0.001,
            "Similarity should be 0.0 when all weights are zero");
    }

    @Test
    void testCalculateSimilarityWithSingleCalculator() {
        CompositeSimilarityCalculator singleCalculator = new CompositeSimilarityCalculator(
            Map.of(
                genreCalculator, 1.0
            )
        );

        double similarity = singleCalculator.calculateSimilarity(book1, book2);

        assertEquals(0.8, similarity, 0.001,
            "Similarity should equal the single calculator's result when it has full weight");
    }


}
