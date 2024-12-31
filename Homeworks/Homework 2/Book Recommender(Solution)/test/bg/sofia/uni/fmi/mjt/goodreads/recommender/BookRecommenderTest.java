package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookRecommenderTest {
    private Set<Book> books;
    private SimilarityCalculator calculator;
    private BookRecommender recommender;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4; // pls don't take points for this i dont want to use iterable.next :(
    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Book 1", "Author 1", "Description 1", List.of("Fiction"), 4.5, 100, "url1");
        book2 = new Book("2", "Book 2", "Author 2", "Description 2", List.of("Fiction", "Adventure"), 4.8, 200, "url2");
        book3 = new Book("3", "Book 3", "Author 3", "Description 3", List.of("Science"), 4.7, 150, "url3");
        book4 = new Book("4", "Book 4", "Author 4", "Description 4", List.of("Fantasy"), 4.2, 110, "url4");
        books = Set.of(
            book1,
            book2,
            book3,
            book4
        );
        calculator = mock(SimilarityCalculator.class);
        recommender = new BookRecommender(books, calculator);
    }

    @Test
    void testrecommendBooksWithNullBookOrigin() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 5)
            , "Expecting IllegalArgumentException for null origin");
    }

    @Test
    void testRecommendBooksWithZeroMaxN() {
        Book origin = book1;

        assertThrows(IllegalArgumentException.class,
            () -> recommender.recommendBooks(origin, 0));
    }

    @Test
    void testRecommendBooksWithLowerMaxN() {
        Book origin = new Book("4", "Book Origin", "Author 4", "Description 4",
            List.of("Fantasy", "Random"), 4.50, 120, "urlOrigin");
        when(calculator.calculateSimilarity(origin, book3)).thenReturn(0.5);

        when(calculator.calculateSimilarity(origin, book2)).thenReturn(0.1);
        when(calculator.calculateSimilarity(origin, book1)).thenReturn(0.3);

        SortedMap<Book, Double> recommended = recommender.recommendBooks(origin, 1);
        assertEquals(1,recommended.size());
    }
    @Test
    void testRecommendBooksWithMoreThanAvailableBooks() {
        Book origin = book1;

        when(calculator.calculateSimilarity(origin, book1)).thenReturn(0.6);
        when(calculator.calculateSimilarity(origin, book2)).thenReturn(0.3);
        when(calculator.calculateSimilarity(origin, book3)).thenReturn(0.5);

        SortedMap<Book, Double> recommended = recommender.recommendBooks(origin, 10);

        assertEquals(books.size() - 1, recommended.size(), // becacause of origin we have -1
            "The number of recommended books should not exceed the number of available books minus 1");
    }

    @Test
    void testRecommendBooksSortingOrder() {
        Book origin = new Book("4", "Book Origin", "Author 4", "Description 4",
            List.of("Fantasy", "Random"), 4.50, 120, "urlOrigin");

        when(calculator.calculateSimilarity(origin, book3)).thenReturn(0.5);
        when(calculator.calculateSimilarity(origin, book4)).thenReturn(0.8);
        when(calculator.calculateSimilarity(origin, book2)).thenReturn(0.1);
        when(calculator.calculateSimilarity(origin, book1)).thenReturn(0.3);

        SortedMap<Book, Double> recommended = recommender.recommendBooks(origin, 2);
        List<String> expectedTitles = recommended.keySet().stream()
            .map(Book::title)
            .toList();

        Double[] similarities = recommended.values().toArray(new Double[0]);
        List<String> titlesToCheck = List.of("Book 4", "Book 3");

        for (int i = 0; i < similarities.length - 1; i++) {
            assertTrue(similarities[i] >= similarities[i + 1],
                "Books should be sorted in descending order of similarity");

            assertEquals(titlesToCheck.get(i), expectedTitles.get(i));
        }
    }

    @Test
    void testRecommendBooksTieBreakingByTitle() {
        Book origin = new Book("4", "Book Origin", "Author 4", "Description 4",
            List.of("Fantasy", "Random"), 4.50, 120, "urlOrigin");

        when(calculator.calculateSimilarity(origin, book3)).thenReturn(0.5);
        when(calculator.calculateSimilarity(origin, book2)).thenReturn(0.7);
        when(calculator.calculateSimilarity(origin, book1)).thenReturn(0.5);


        SortedMap<Book, Double> recommended = recommender.recommendBooks(origin, 3);

        Book[] sortedBooks = recommended.keySet().toArray(new Book[0]);
        for (int i = 0; i < sortedBooks.length - 1; i++) {
            if (recommended.get(sortedBooks[i]).equals(recommended.get(sortedBooks[i + 1]))) {
                assertTrue(sortedBooks[i].title().compareTo(sortedBooks[i + 1].title()) <= 0,
                    "Books with the same similarity score should be sorted lexicographically by title");
            }
        }
    }
}
