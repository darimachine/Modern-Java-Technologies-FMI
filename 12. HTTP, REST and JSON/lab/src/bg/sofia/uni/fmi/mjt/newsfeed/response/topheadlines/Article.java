package bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines;

public record Article(Source source, String author, String title, String description, String url,
                      String urlToImage, String publishedAt, String content) {
}
