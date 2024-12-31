package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookFinderTest {

    private BookFinder bookFinder;
    private TextTokenizer tokenizer;
    private Set<Book> books;

    @BeforeEach
    void setUp() {
        books = Set.of(
            new Book("1", "Book 1", "Author 1",
                "Description of book one", List.of("Fiction", "Adventure"),
                4.5, 100, "url1"),
            new Book("2", "Book 2", "Author 2",
                "Another description for book 2", List.of("Fantasy"),
                4.8, 200, "url2"),
            new Book("3", "Book 3", "Author 1",
                "more details for book 3 here", List.of("Adventure", "Fiction"),
                4.7, 150, "url3")
        );

        tokenizer = mock();
        when(tokenizer.tokenize("book 1 description of book one")).thenReturn(
            List.of("book", "1", "description", "of", "one"));
        when(tokenizer.tokenize("book 2 another description for book 2")).thenReturn(
            List.of("book", "2", "another", "description"));
        when(tokenizer.tokenize("book 3 more details for book 3 here")).thenReturn(
            List.of("book", "3", "more", "details", "here"));

        bookFinder = new BookFinder(books, tokenizer);
    }

    @Test
    void testAllBooks() {
        Set<Book> allBooks = bookFinder.allBooks();
        assertEquals(3, allBooks.size(), "Book size must be 3");
        assertTrue(allBooks.containsAll(books), "books in bookFinder must contain all the books");
    }

    @Test
    void testSearchByAuthorValid() {
        List<Book> booksByAuthor = bookFinder.searchByAuthor("Author 1");
        assertEquals(2, booksByAuthor.size(), "Books by Author 1 should return two books");
        boolean isAllBooksWithSameAuthor = booksByAuthor.stream()
            .allMatch(book -> book.author().equals("Author 1"));
        assertTrue(isAllBooksWithSameAuthor, "All books returned should be authored by Author 1");
    }

    @Test
    void testSearchByAuthorInvalid() {
        List<Book> booksByAuthor = bookFinder.searchByAuthor("Not existed author");
        assertTrue(booksByAuthor.isEmpty(), "Search for a non-existent author should return no books");
    }

    @Test
    void testSearchByAuthorNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null),
            "Search by author should throw an exception if the author name is null");
    }

    @Test
    void testSearchByAuthorEmpty() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(""),
            "Search by author should throw an exception if the author name is empty");
    }

    @Test
    void testAllGenres() {
        Set<String> genres = bookFinder.allGenres();
        assertEquals(Set.of("Fiction", "Adventure", "Fantasy"), genres,
            "All unique genres should be included from all books");
    }

    @Test
    void testSearchByGenresMatchAll() {
        List<Book> booksByGenres = bookFinder.searchByGenres(Set.of("Fiction", "Adventure"), MatchOption.MATCH_ALL);
        assertEquals(2, booksByGenres.size(), "Match all should return books containing both genres");

    }

    @Test
    void testSearchByGenresMatchAny() {
        List<Book> booksByGenres = bookFinder.searchByGenres(Set.of("Fantasy", "Adventure"), MatchOption.MATCH_ANY);
        assertEquals(3, booksByGenres.size(), "Match any should return books containing at least one genre");
    }

    @Test
    void testSearchByGenresEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(Set.of(), MatchOption.MATCH_ALL),
            "Search by genres should throw an exception if genres set is empty");
    }

    @Test
    void testSearchByGenresNullInput() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ANY),
            "Search by genres should throw an exception if genre set is null");
    }

    @Test
    void testSearchByKeywordsMatchAll() {
        Set<String> keywords = Set.of("book", "description");
        List<Book> booksByKeywords = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);

        assertEquals(2, booksByKeywords.size(), "Match all should return books matching all keywords");
        Set<String> keywords2 = Set.of("book", "details");
        List<Book> booksByKeywords2 = bookFinder.searchByKeywords(keywords2, MatchOption.MATCH_ALL);

        assertEquals(1, booksByKeywords2.size(),
            "Match all should return books matching all keywords");

        assertEquals("Book 3", booksByKeywords2.getFirst().title(),
            "Returned book should match the correct criteria");

    }

    @Test
    void testSearchByKeywordsMatchAny() {
        Set<String> keywords = Set.of("book", "details");
        List<Book> booksByKeywords = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);

        assertEquals(3, booksByKeywords.size(),
            "Match any should return books matching at least one keyword");
    }

    @Test
    void testSearchByKeywordsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(Set.of(), MatchOption.MATCH_ANY),
            "Search by keywords should throw an exception if keywords set is empty");
    }

    @Test
    void testSearchByKeywordsNullInput() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ANY),
            "Search by keywords should throw an exception if keywords set is null");
    }
}
