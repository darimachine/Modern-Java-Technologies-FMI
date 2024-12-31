package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TFIDFSimilarityCalculatorTest {
    private List<Book> books;
    private TextTokenizer tokenizer;
    private TFIDFSimilarityCalculator calculator;

    @BeforeEach
    void setUp() {
        books = List.of(
            new Book("1", "Book 1", "Author 1",
                "academy superhero is club superhero", List.of("Programming"),
                4.5, 1000, "url1"),
            new Book("2", "Book 2", "Author 2",
                "superhero and his mission to save club", List.of("Programming", "Web Developer"),
                4.8, 2000, "url2"),
            new Book("3", "Book 3", "Author 3",
                "crime murder for mystery club", List.of("Programming", "Data Science"),
                4.7, 1500, "url3")
        );
        tokenizer = mock();
        when(tokenizer.tokenize("academy superhero is club superhero"))
            .thenReturn(List.of("academy", "superhero", "club", "superhero"));
        when(tokenizer.tokenize("superhero and his mission to save club"))
            .thenReturn(List.of("superhero", "mission", "save", "club"));
        when(tokenizer.tokenize("crime murder for mystery club"))
            .thenReturn(List.of("crime", "murder", "mystery", "club"));
        calculator = new TFIDFSimilarityCalculator(new HashSet<>(books), tokenizer);
    }
    @Test
    void testCalculateSimilarityWithNullSecondBook() {
        Book book1 = books.getFirst(); // First book
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null));
    }
    @Test
    void testComputeTFWithNull() {
        assertThrows(IllegalArgumentException.class, () -> calculator.computeTF(null),
            "It should throw IllegalArgumentException for null Book on TF");
    }

    @Test
    void testComputeTFWithCorrect() {
        Book book = books.get(0); // First book
        var tfScores = calculator.computeTF(book);

        assertEquals(2.0 / 4.0, tfScores.get("superhero"), 0.001,
            "TF score for 'superhero' should be correct");
        assertEquals(1.0 / 4.0, tfScores.get("academy"), 0.001,
            "TF score for 'academy' should be correct");
        assertEquals(1.0 / 4.0, tfScores.get("club"), 0.001,
            "TF score for 'club' should be correct");
        assertNull(tfScores.get("mystery"),
            "TF score for an absent word should be null");
    }

    @Test
    void testComputeIDFWithNull() {

        assertThrows(IllegalArgumentException.class, () -> calculator.computeIDF(null),
            "It should throw IllegalArgumentException for null Book on IDF");
    }

    @Test
    void testComputeIDFWithCorrect() {

        var idfScores = calculator.computeIDF(books.getFirst());

        assertEquals(Math.log10(3.0 / 2.0), idfScores.get("superhero"), 0.001,
            "IDF score for 'superhero' should be correct");
        assertEquals(Math.log10(3.0 / 3.0), idfScores.get("club"), 0.001,
            "IDF score for 'club' should be correct");
        assertEquals(Math.log10(3.0 / 1.0), idfScores.get("academy"), 0.001,
            "IDF score for 'academy' should be correct");
    }

    @Test
    void testComputeTFIDFWithNull() {
        assertThrows(IllegalArgumentException.class, () -> calculator.computeTFIDF(null),
            "It should throw IllegalArgumentException for null Book on TF+IDF");
    }

    @Test
    void testComputeTFIDFWithCorrect() {
        Book book = books.get(0); // First book
        var tfIdfScores = calculator.computeTFIDF(book);

        double tfSuperhero = 2.0 / 4.0;
        double idfSuperhero = Math.log10(3.0 / 2.0);
        assertEquals(tfSuperhero * idfSuperhero, tfIdfScores.get("superhero"), 0.001,
            "TF-IDF score for 'superhero' should be correct");

        double tfAcademy = 1.0 / 4.0;
        double idfAcademy = Math.log10(3.0 / 1.0);
        assertEquals(tfAcademy * idfAcademy, tfIdfScores.get("academy"), 0.001,
            "TF-IDF score for 'academy' should be correct");
    }


    @Test
    void testCalculateSimilarityIdenticalBooks() {
        Book book1 = books.get(0);

        double similarity = calculator.calculateSimilarity(book1, book1);
        assertEquals(1.00, similarity, 0.001,
            "Similarity of 2 identical books must be 1");
    }

    @Test
    void testCalculateSimilarityNoOverlap() {

        Book book1 = books.get(0);
        Book book3 = books.get(2);
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertEquals(0.0, similarity, 0.001,
            "Similarity of 2 books with dont have even one word must be 0");

    }

    @Test
    void testCalculateSimilarityOverlap() {
        Book book1 = books.get(0);
        Book book3 = books.get(1);
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertTrue(similarity > 0.00,
            "Similarity of 2 books has overlaping word is must in the range (0,1]");
        assertTrue(similarity <= 1.0,
            "Similarity of 2 books has overlaping word is must in the range (0,1]");
    }

    @Test
    void testCalculateSimilarityWithNullFirstBook() {
        Book book2 = books.get(1); // Second book
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book2));
    }




}
