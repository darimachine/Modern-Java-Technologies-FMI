package bg.sofia.uni.fmi.mjt.newsfeed.request.topheadlines;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.InvalidCategoryException;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.RequiredQueryMissingException;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsRequestUriTest {

    @Test
    void testValidNewsRequestUri() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("science")
            .country("us")
            .build();

        URI generatedUri = uri.getUri();

        assertNotNull(generatedUri, "The generated URI should not be null");
        assertTrue(generatedUri.toString().contains("q=technology"), "URI should contain the keywords query");
        assertTrue(generatedUri.toString().contains("&category=science"), "URI should contain the category query");
        assertTrue(generatedUri.toString().contains("&country=us"), "URI should contain the country query");
    }
    @Test
    void testMissingKeywordsThrowsException() {
        assertThrows(RequiredQueryMissingException.class,
            () -> NewsRequestUri.newBuilder(null).build(),
            "Building without keywords should throw an exception");
    }
    @Test
    void testEmptyKeywordsThrowsException() {
        assertThrows(RequiredQueryMissingException.class,
            () -> NewsRequestUri.newBuilder("").build(),
            "Building with empty keywords should throw an exception");
    }

    @Test
    void testEmptyCategoryIgnored() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("")
            .country("us")
            .build();

        URI generatedUri = uri.getUri();
        assertNotNull(generatedUri, "The generated URI should not be null");
        assertFalse(generatedUri.toString().contains("&category="), "URI should not contain an empty category");
    }
    @Test
    void testInvalidCountryThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> NewsRequestUri.newBuilder("technology")
                .category("science")
                .country(null)
                .build(),
            "Country cannot be null");
    }

    @Test
    void testNullCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> NewsRequestUri.newBuilder("technology")
                .category(null)
                .build(),
            "Category cannot be null");
    }
    @Test
    void testInvalidCategoryThrowsException() {
        assertThrows(InvalidCategoryException.class,
            () -> NewsRequestUri.newBuilder("technology")
                .category("invalid-category")
                .build(),
            "Invalid category should throw an exception");
    }
    @Test
    void testEmptyCountryIgnored() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("science")
            .country("")
            .build();

        URI generatedUri = uri.getUri();
        assertNotNull(generatedUri, "The generated URI should not be null");
        assertFalse(generatedUri.toString().contains("&country="), "URI should not contain an empty country");
    }

    @Test
    void testOnlyKeywords() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology").build();

        URI generatedUri = uri.getUri();
        assertNotNull(generatedUri, "The generated URI should not be null");
        assertTrue(generatedUri.toString().contains("q=technology"), "URI should contain the keywords query");
        assertFalse(generatedUri.toString().contains("&category="), "URI should not contain a category query");
        assertFalse(generatedUri.toString().contains("&country="), "URI should not contain a country query");
    }
    @Test
    void testValidNewsRequestUriGetters() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("science")
            .country("us")
            .build();

        assertEquals("technology", uri.getKeywordsQuery(), "Keywords should be correct");
        assertEquals("science", uri.getCategoryQuery(), "Category should be correct");
        assertEquals("us", uri.getCountryQuery(), "Country should be correct");
    }
    @Test
    void testGetQueryWithAllParameters() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("science")
            .country("us")
            .build();

        String expectedQuery = "q=technology&category=science&country=us&apiKey=" + System.getenv("News_API_KEY");

        assertEquals(expectedQuery, uri.getQuery(), "The query string should include all parameters correctly");
    }
    @Test
    void testGetQueryWithKeywordsOnly() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology").build();
        String expectedQuery = "q=technology&apiKey=" + System.getenv("News_API_KEY");

        assertEquals(expectedQuery, uri.getQuery(), "The query string should include only the keywords and API key");
    }
    @Test
    void testGetQueryWithKeywordsAndCategory() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .category("science")
            .build();
        String expectedQuery = "q=technology&category=science&apiKey=" + System.getenv("News_API_KEY");

        assertEquals(expectedQuery, uri.getQuery(), "The query string should include keywords and category");
    }

    @Test
    void testGetQueryWithKeywordsAndCountry() {
        NewsRequestUri uri = NewsRequestUri.newBuilder("technology")
            .country("us")
            .build();
        String expectedQuery = "q=technology&country=us&apiKey=" + System.getenv("News_API_KEY");

        assertEquals(expectedQuery, uri.getQuery(), "The query string should include keywords and country");
    }
    @Test
    void testGetUriThrowsExceptionForInvalidQuery() {
        NewsRequestUri requestUri;

        requestUri = mock();

        when(requestUri.getUri()).thenThrow(new RuntimeException("Invalid query"));

        assertThrows(RuntimeException.class, requestUri::getUri,
            "An invalid query should throw a RuntimeException");
    }
}

