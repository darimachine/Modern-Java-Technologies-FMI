package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextTokenizerTest {

    private TextTokenizer tokenizer;

    @BeforeEach
    void setUp() {

        String stopword = "the\n" +
            "from\n" +
            "book\n" +
            "descrtiption\n" +
            "is";
        this.tokenizer = new TextTokenizer(new StringReader(stopword));
    }

    @Test
    void testTokenizeValidInput() {
        String input = "THIS is, the, SAMple <><<!   input for    book.";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("this", "sample", "input", "for");
        assertEquals(expected, tokens, "Tokenize should correctly process the input string");
    }

    @Test
    void testTokenizeWithStopwords() {
        String input = "The quick brown fox, jumps over the lazy dog.";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("quick", "brown", "fox", "jumps", "over", "lazy", "dog");
        assertEquals(expected, tokens, "Tokenize should correctly remove stopwords");
    }

    @Test
    void testTokenizeWithPunctuation() {
        String input = "   Hello, world!  This, .<>  is a test.    ";
        List<String> tokens = tokenizer.tokenize(input);

        List<String> expected = List.of("hello", "world", "this", "a", "test");
        assertEquals(expected, tokens, "Tokenize should correctly remove punctuation");
    }

    @Test
    void testTokenizeEmptyInput() {
        String input = "";
        List<String> tokens = tokenizer.tokenize(input);

        assertTrue(tokens.isEmpty(), "Tokenize should return an empty list for empty input");
    }

    @Test
    void testTokenizeNullInput() {
        List<String> tokens = tokenizer.tokenize(null);
        assertTrue(tokens.isEmpty(), "Tokenize should return an empty list for null input");
    }

    @Test
    void testTokenizeWhitespaceInput() {
        String input = "    ";
        List<String> tokens = tokenizer.tokenize(input);

        assertTrue(tokens.isEmpty(), "Tokenize should return an empty list for whitespace-only input");
    }

    @Test
    void testTokenizeWithConsecutiveSpaces() {
        String input = "BookS   has  multiple     spaces   between   words.";
        List<String> tokens = tokenizer.tokenize(input);


        List<String> expected = List.of("books", "has", "multiple", "spaces", "between", "words");
        assertEquals(expected, tokens, "Tokenize should correctly handle multiple consecutive spaces");
    }

    @Test
    void testStopwordsLoadedCorrectly() {
        Set<String> stopwords = tokenizer.stopwords();

        assertTrue(stopwords.contains("the"), "Stopwords should contain 'the'");
        assertTrue(stopwords.contains("is"), "Stopwords should contain 'is'");
        assertTrue(stopwords.contains("from"), "Stopwords should contain 'from'");
        assertTrue(stopwords.contains("book"), "Stopwords should contain 'book'");
    }

    @Test
    void testStopwordsNotContainingExtraWords() {
        Set<String> stopwords = tokenizer.stopwords();

        assertFalse(stopwords.contains("hello"), "Stopwords should not contain 'hello'");
        assertFalse(stopwords.contains("world"), "Stopwords should not contain 'world'");
    }
}
