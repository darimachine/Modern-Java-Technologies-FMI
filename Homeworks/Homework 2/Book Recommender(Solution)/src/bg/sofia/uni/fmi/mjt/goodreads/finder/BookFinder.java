package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            throw new IllegalArgumentException("authorName is Null or Empty");
        }
        List<Book> booksByAuthor = books.stream()
            .filter(e -> e.author().equals(authorName))
            .toList();
        return booksByAuthor;
    }

    @Override
    public Set<String> allGenres() {
        Set<String> allGenres = books.stream()
            .flatMap(e -> e.genres().stream())
            .collect(Collectors.toSet());
        return allGenres;
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres is null");
        }

        return books.stream()
            .filter(e -> matchesGenre(e, genres, option))
            .toList();

    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords cannot be null or empty.");
        }
        Set<String> lowerCaseKeywords = keywords.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        return books.stream()
            .filter(book -> matchesKeywords(book, lowerCaseKeywords, option))
            .toList();

    }

    private boolean matchesKeywords(Book book, Set<String> keywords, MatchOption option) {
        List<String> tokens = tokenizer.tokenize(book.title().toLowerCase() + " " + book.description().toLowerCase());
        if (option == MatchOption.MATCH_ALL) {
            return tokens.containsAll(keywords);
        } else if (option == MatchOption.MATCH_ANY) {
            return keywords.stream().anyMatch(tokens::contains);
        }
        return false;
    }

    private boolean matchesGenre(Book book, Set<String> genres, MatchOption option) {
        Set<String> genresByBook = new HashSet<>(book.genres());
        if (option == MatchOption.MATCH_ALL) {
            return genresByBook.containsAll(genres);
        } else if (option == MatchOption.MATCH_ANY) {
            return genresByBook.stream().anyMatch(e -> genres.contains(e));
        }
        return false;
    }
}
