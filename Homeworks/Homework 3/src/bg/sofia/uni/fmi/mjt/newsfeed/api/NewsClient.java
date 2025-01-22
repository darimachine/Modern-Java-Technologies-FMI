package bg.sofia.uni.fmi.mjt.newsfeed.api;

import com.google.gson.Gson;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.RequestFailedException;
import bg.sofia.uni.fmi.mjt.newsfeed.json.GsonSingleton;

import bg.sofia.uni.fmi.mjt.newsfeed.request.topheadlines.NewsRequestUri;
import bg.sofia.uni.fmi.mjt.newsfeed.response.ResponseValidator;
import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.NewsApiResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewsClient implements NewsClientAPI {
    private static final int DEFAULT_PAGE_SIZE = 20; // Default value if not provided
    private static final String PAGE_QUERY_PREFIX = "&page=";
    private static final int MAX_PAGES = 5;
    private final HttpClient httpClient;
    Gson gson = GsonSingleton.getInstance();

    public NewsClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public NewsClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Collection<Article> getArticles(String keywords, String category, String county) {
        return fetchArticles(keywords, category, county);
    }

    @Override
    public Collection<Article> getArticles(String keywords, String category) {
        return fetchArticles(keywords, category, "");
    }

    @Override
    public Collection<Article> getArticles(String keywords) {
        return fetchArticles(keywords, "", "");
    }

    @Override
    public String convertArticlesToJson(Collection<Article> articles) {
        return gson.toJson(articles);
    }

    private Collection<Article> fetchArticles(String keywords, String category, String country) {
        URI uri = NewsRequestUri.newBuilder(keywords)
            .category(category)
            .country(country)
            .build()
            .getUri();
        List<Article> articles = new ArrayList<>();

        fetchArticleFromEachPage(uri, articles);

        return articles;
    }

    private void fetchArticleFromEachPage(URI uri, List<Article> articles) {
        for (int currentPage = 1; currentPage <= MAX_PAGES; currentPage++) {
            HttpResponse<String> response = sendRequest(uri);
            ResponseValidator.validateResponse(response);
            NewsApiResponse newsApiResponse = gson.fromJson(response.body(), NewsApiResponse.class);
            articles.addAll(newsApiResponse.articles());
            if (currentPage * DEFAULT_PAGE_SIZE >= newsApiResponse.totalResults()) {
                break;
            }
            uri = URI.create(uri.toString() + PAGE_QUERY_PREFIX + (currentPage + 1));
        }
    }

    private HttpResponse<String> sendRequest(URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RequestFailedException("Failed to send request", e);
        }

    }
}
