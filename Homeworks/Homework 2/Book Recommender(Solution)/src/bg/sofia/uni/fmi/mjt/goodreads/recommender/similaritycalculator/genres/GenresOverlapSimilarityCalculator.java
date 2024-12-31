package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("first or second book is null");
        }
        int firstBookGenresSize = first.genres().size();
        int secondBookGenresSize = second.genres().size();
        if (firstBookGenresSize == 0 || secondBookGenresSize == 0) {
            return 0.0;
        }
        Set<String> firstGenres = new HashSet<>(first.genres());
        Set<String> secondGenres = new HashSet<>(second.genres());
        firstGenres.retainAll(secondGenres);
        int commonGenres = firstGenres.size();
        int smallerSetSize = Math.min(firstBookGenresSize, secondBookGenresSize);
        return (double) commonGenres / smallerSetSize;
    }
}
