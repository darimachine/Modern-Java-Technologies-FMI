package bg.sofia.uni.fmi.mjt.newsfeed.request.topheadlines;

import bg.sofia.uni.fmi.mjt.newsfeed.enums.Category;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.RequiredQueryMissingException;

import java.net.URI;

public class NewsRequestUri {

    private static final String API_SCHEME = "https";
    private static final String API_HOST = "newsapi.org";
    private static final String API_PATH = "/v2/top-headlines";
    private static final String APP_KEY_QUERY = "&apiKey=" + System.getenv("News_API_KEY");
    private static final String KEYWORDS_QUERY_PREFIX = "q=";
    private static final String CATEGORY_QUERY_PREFIX = "&category=";
    private static final String COUNTRY_QUERY_PREFIX = "&country=";

    private final String keywordsQuery;
    private final String countryQuery;
    private final Category categoryQuery;

    private NewsRequestUri(NewsRequestUriBuilder builder) {
        this.keywordsQuery = builder.keywordsQuery;
        this.countryQuery = builder.countryQuery;
        this.categoryQuery = builder.categoryQuery;
    }

    public static NewsRequestUriBuilder newBuilder(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new RequiredQueryMissingException("Keywords are required and cannot be null or empty.");
        }
        return new NewsRequestUriBuilder(keywords);
    }

    public String getKeywordsQuery() {
        return keywordsQuery;
    }

    public String getCountryQuery() {
        return countryQuery;
    }

    public String getCategoryQuery() {
        return categoryQuery.getValue();
    }

    public String getQuery() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(KEYWORDS_QUERY_PREFIX).append(keywordsQuery);

        if (categoryQuery != null && !categoryQuery.getValue().isBlank()) {
            urlBuilder.append(CATEGORY_QUERY_PREFIX).append(categoryQuery.getValue());
        }
        if (countryQuery != null && !countryQuery.isBlank()) {
            urlBuilder.append(COUNTRY_QUERY_PREFIX).append(countryQuery);
        }
        urlBuilder.append(APP_KEY_QUERY);

        return urlBuilder.toString();
    }

    public URI getUri() {
        try {
            return new URI(API_SCHEME, API_HOST, API_PATH, getQuery(), null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create URI", e);
        }
    }

    public static class NewsRequestUriBuilder {

        private final String keywordsQuery;
        private Category categoryQuery;
        private String countryQuery;

        private NewsRequestUriBuilder(String keywords) {
            this.keywordsQuery = keywords;
        }

        public NewsRequestUriBuilder category(String category) {
            if (category == null) {
                throw new IllegalArgumentException("Category is required and cannot be null or empty.");
            }
            //The idea behind this is to call all the optional functions in the CLIENT
            // to do not have repeated code
            if (!category.isBlank()) {
                this.categoryQuery = Category.fromString(category);
            }
            return this;
        }

        public NewsRequestUriBuilder country(String country) {
            if (country == null) {
                throw new IllegalArgumentException("Country is required and cannot be null or empty.");
            }
            // same idea as the category
            if (!country.isBlank()) {
                this.countryQuery = country;
            }
            return this;
        }

        public NewsRequestUri build() {
            return new NewsRequestUri(this);
        }
    }

}
