package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookLoaderTest {

    @Test
    void testLoadWithValidInput() {

        String validCSVInput = "N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL\n" +
            "0,To Kill a Mockingbird,Harper Lee,\"Description 1.\",\"['Classics', 'Fiction', 'Historical Fiction']\",4.27,\"5,691,311\",url1\n" +
            "1,\"Harry Potter\",J.K. Rowling,\"Description 2\",\"['Fantasy', 'Fiction', 'Magic']\",4.47,\"9,278,135\",url2\n" +
            "2,Pride and Prejudice,Jane Austen,\"Description 3\",\"['Classics', 'Fiction', 'Romance']\",4.28,\"3,944,155\",url3";
        Set<Book> books = BookLoader.load(new StringReader(validCSVInput));
        assertEquals(3, books.size(),
            "The number of loaded books should match the number of valid entries in the CSV.");
        assertTrue(books.stream().anyMatch(book -> book.title().equals("Harry Potter")),
            "The loaded books should contain 'Harry Potter'.");
        assertTrue(books.stream().anyMatch(book -> book.genres().contains("Fantasy")),
            "The loaded books should contain a book with genre 'Fantasy'.");
        assertTrue(books.stream().allMatch(book -> book.genres().contains("Fiction")),
            "All the loaded books should contain genre 'Fiction'");
    }

    @Test
    void testLoadEmptyCSV() {
        String emptyCSV = """
            N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL
            """;

        Set<Book> books = BookLoader.load(new StringReader(emptyCSV));

        assertTrue(books.isEmpty(), "Loading an empty CSV should return an empty set.");
    }

    @Test
    void testLoadInvalidCSVFormat() {
        String invalidCSV = """
            N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL
            5,Book 1,Description 1,"[Fiction, Adventure]",4.5,1000,url1
            """;

        assertThrows(IllegalArgumentException.class, () -> BookLoader.load(new StringReader(invalidCSV)),
            "Loading a CSV with an invalid header should throw IllegalArgumentException.");
    }

    @Test
    void testLoadWithMispeleldQuote() {
        String malformedCSV = """
            N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL
            1,Book 1,Author 1",Description 1,"[Fiction, Adventure]",4.5,1000,url1
            """;

        assertThrows(IllegalArgumentException.class, () -> BookLoader.load(new StringReader(malformedCSV)),
            "Loading a malformed CSV should throw IllegalArgumentException.");

    }

    @Test
    void testLoadNullReader() {
        assertThrows(NullPointerException.class,
            () -> BookLoader.load(null),
            "Loading with a null reader should throw NullPointerException.");
    }
}
