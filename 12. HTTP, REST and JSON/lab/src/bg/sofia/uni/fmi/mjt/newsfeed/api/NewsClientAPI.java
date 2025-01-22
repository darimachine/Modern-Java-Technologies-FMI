package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.Article;

import java.util.Collection;

public interface NewsClientAPI {

    Collection<Article> getArticles(String keywords, String category, String county);

    Collection<Article> getArticles(String keywords, String category);

    Collection<Article> getArticles(String keywords);

    String convertArticlesToJson(Collection<Article> recipes);
}
