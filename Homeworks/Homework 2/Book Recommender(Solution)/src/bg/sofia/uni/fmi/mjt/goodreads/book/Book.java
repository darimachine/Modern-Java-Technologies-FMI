package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {

    public static Book of(String[] tokens) {
        int idx = 0;

        String id = tokens[idx++];
        String title = tokens[idx++];
        String author = tokens[idx++];
        String description = tokens[idx++];
        //System.out.println(tokens[idx]);
        List<String> genres = Arrays.stream(tokens[idx++]
                .replaceAll("[\\[\\]']", "")
                .split(","))
            .map(String::trim)
            .toList();
        double rating = Double.parseDouble(tokens[idx++]);
        int ratingCount = Integer.parseInt(tokens[idx++].replace(",", ""));
        String url = tokens[idx];
        return new Book(id, title, author, description, genres, rating, ratingCount, url);

    }
}
