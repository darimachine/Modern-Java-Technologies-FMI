import bg.sofia.uni.fmi.mjt.newsfeed.api.NewsClient;
import bg.sofia.uni.fmi.mjt.newsfeed.api.NewsClientAPI;
import bg.sofia.uni.fmi.mjt.newsfeed.json.JsonFileSaver;
import bg.sofia.uni.fmi.mjt.newsfeed.request.topheadlines.NewsRequestUri;
import bg.sofia.uni.fmi.mjt.newsfeed.response.topheadlines.Article;

import java.util.Collection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        NewsClientAPI client = new NewsClient();
        Collection<Article> articles = client.getArticles("trump+inauguration", "");
        System.out.println(articles);
        String json = client.convertArticlesToJson(articles);
        JsonFileSaver.saveJsonToFile(json, "articles.json");
    }
}