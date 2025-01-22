package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.RequestFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.Source;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.net.http.HttpResponse.BodyHandler;

public class NewsClientTest {
    private HttpClient httpClientMock;
    private HttpResponse<String> httpResponseMock;
    private NewsClient newsClient;

    @BeforeEach
    void setUp() {
        httpClientMock = mock();
        httpResponseMock = mock();
        newsClient = new NewsClient(httpClientMock);
    }

    @Test
    void testGetArticlesWithAllParameters() throws Exception {

        String jsonResponse = """
            {
                "status": "ok",
                "totalResults": 2,
                "articles": [
                    {"source": {"id": "abc", "name": "ABC News"}, "author": "John Doe", "title": "Test Article 1",
                     "description": "Description 1", "url": "https://example.com", "urlToImage": "https://image.com/1",
                      "publishedAt": "2025-01-15T10:00:00Z", "content": "Content 1"},
                    {"source": {"id": "xyz", "name": "XYZ News"}, "author": "Jane Doe", "title": "Test Article 2",
                     "description": "Description 2", "url": "https://example.com", "urlToImage": "https://image.com/2",
                      "publishedAt": "2025-01-15T11:00:00Z", "content": "Content 2"}
                ]
            }
            """;

        when(httpResponseMock.body()).thenReturn(jsonResponse);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpClientMock.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(
            httpResponseMock);


        Collection<Article> articles = newsClient.getArticles("technology", "science", "us");

        assertNotNull(articles, "Articles collection should not be null");
        assertEquals(2, articles.size(), "There should be 2 articles in the collection");
    }

    @Test
    void testGetArticlesWithAllParametersExceptCountry() throws Exception {

        String jsonResponse = """
            {
                "status": "ok",
                "totalResults": 2,
                "articles": [
                    {"source": {"id": "abc", "name": "ABC News"}, "author": "John Doe", "title": "Test Article 1",
                     "description": "Description 1", "url": "https://example.com", "urlToImage": "https://image.com/1",
                      "publishedAt": "2025-01-15T10:00:00Z", "content": "Content 1"},
                    {"source": {"id": "xyz", "name": "XYZ News"}, "author": "Jane Doe", "title": "Test Article 2",
                     "description": "Description 2", "url": "https://example.com", "urlToImage": "https://image.com/2",
                      "publishedAt": "2025-01-15T11:00:00Z", "content": "Content 2"}
                ]
            }
            """;

        when(httpResponseMock.body()).thenReturn(jsonResponse);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpClientMock.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(
            httpResponseMock);

        Collection<Article> articles = newsClient.getArticles("technology", "science");

        assertNotNull(articles, "Articles collection should not be null");
        assertEquals(2, articles.size(), "There should be 2 articles in the collection");
    }
    @Test
    void testConvertArticlesToJson() {

        List<Article> articles = new ArrayList<>();
        articles.add(new Article(new Source("abc", "ABC News"), "John Doe", "Test Article 1", "Description 1",
            "https://example.com", "https://image.com/1", "2025-01-15T10:00:00Z", "Content 1"));
        articles.add(new Article(new Source("xyz", "XYZ News"), "Jane Doe", "Test Article 2", "Description 2",
            "https://example.com", "https://image.com/2", "2025-01-15T11:00:00Z", "Content 2"));

        String json = newsClient.convertArticlesToJson(articles);

        assertNotNull(json, "JSON string should not be null");
        assertTrue(json.contains("\"author\":\"John Doe\""), "JSON should contain the first article's author");
        assertTrue(json.contains("\"author\":\"Jane Doe\""), "JSON should contain the second article's author");
    }

    @Test
    void testFetchHandlesValidationFailure() throws Exception {
        when(httpResponseMock.statusCode()).thenReturn(400);
        when(httpResponseMock.body()).thenReturn("{\"status\":\"error\",\"code\":\"badRequest\",\"message\":\"Invalid query\"}");
        when(httpClientMock.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(httpResponseMock);

        NewsClient newsClient = new NewsClient(httpClientMock);

        assertThrows(RequestFailedException.class, () -> newsClient.getArticles("technology"),
            "RequestFailedException should be thrown for validation failure");
    }

    @Test
    void testPaginationStopsAfterAllArticlesRetrieved() throws Exception {

        String jsonResponse = """
            {
                "status": "ok",
                "totalResults": 2,
                "articles": [
                    {"source": {"id": "abc", "name": "ABC News"},
                    "author": "John Doe", "title": "Test Article 1", "description": "Description 1",
                    "url": "https://example.com", "urlToImage": "https://image.com/1",
                    "publishedAt": "2025-01-15T10:00:00Z", "content": "Content 1"}
                ]
            }
            """;

        when(httpResponseMock.body()).thenReturn(jsonResponse);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpClientMock.send(any(HttpRequest.class), any(BodyHandler.class))).thenReturn(
            httpResponseMock);

        Collection<Article> articles = newsClient.getArticles("technology");

        assertNotNull(articles, "Articles collection should not be null");
        assertEquals(1, articles.size(), "Pagination should stop after retrieving all articles");
    }

    @Test
    void testRequestFailedException() throws Exception {

        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(
            new RuntimeException("Test exception"));

        assertThrows(RequestFailedException.class, () -> newsClient.getArticles("technology"),
            "RequestFailedException should be thrown if the HTTP request fails");
    }
    @Test
    void testFetchAllPagesSuccessfully() throws Exception {

        String jsonResponsePage1 = """
        {
            "status": "ok",
            "totalResults": 30,
            "articles": [
                {"source": {"id": "abc", "name": "ABC News"},
                "author": "John Doe", "title": "Article 1", "description": "Description 1",
                "url": "https://example.com/1", "urlToImage": "https://image.com/1",
                "publishedAt": "2025-01-15T10:00:00Z", "content": "Content 1"}
            ]
        }
    """;
        String jsonResponsePage2 = """
        {
            "status": "ok",
            "totalResults": 3,
            "articles": [
                {"source": {"id": "xyz", "name": "XYZ News"},
                "author": "Jane Doe", "title": "Article 2", "description": "Description 2",
                "url": "https://example.com/2", "urlToImage": "s://image.com/2",
                "publishedAt": "2025-01-15T11:00:00Z", "content": "Content 2"}
            ]
        }
    """;

        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body())
            .thenReturn(jsonResponsePage1)
            .thenReturn(jsonResponsePage2); // Simulate two pages
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(httpResponseMock);

        NewsClient newsClient = new NewsClient(httpClientMock);

        Collection<Article> articles = newsClient.getArticles("technology");

        assertNotNull(articles, "Articles collection should not be null");
        assertEquals(2, articles.size(), "All articles from both pages should be fetched");
    }
}
