package bg.sofia.uni.fmi.mjt.sentimentanalyzer.lexicon;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SentimentLexiconLoader {

    public static Map<String, SentimentScore> loadLexicon(Path filePath) {
        Map<String, SentimentScore> sentimentMap = new HashMap<>();
        try (var reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    SentimentScore sentimentScore = SentimentScore.fromScore(score);
                    sentimentMap.put(word, sentimentScore);
                }

            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error reading", ex);
        }
        return sentimentMap;
    }
}
