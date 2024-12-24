package bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer;

import java.util.Set;
import java.util.List;
import java.util.stream.Stream;

public class TextTokenizer {
    private final Set<String> stopwords;

    public TextTokenizer(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<String> tokenize(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }
        String formattedInput = input.replaceAll("\\p{Punct}", "")
            .replaceAll("\\s+", " ")
            .toLowerCase()
            .strip();
        return Stream.of(formattedInput.split(" "))
            .filter(word -> !stopwords.contains(word))
            .filter(word -> !word.isBlank())
            .toList();
    }
}
