package bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines;

import java.util.List;

public record NewsApiResponse(String status, int totalResults, List<Article> articles) {
}
