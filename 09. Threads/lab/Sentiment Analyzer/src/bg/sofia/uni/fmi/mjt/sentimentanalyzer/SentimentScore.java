package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

public enum SentimentScore {
    VERY_NEGATIVE(-5, "Very Negative"),
    NEGATIVE(-4, "Negative"),
    MODERATELY_NEGATIVE(-3, "Moderately Negative"),
    SLIGHTLY_NEGATIVE(-2, "Slightly Negative"),
    WEAKLY_NEGATIVE(-1, "Weakly Negative"),
    NEUTRAL(0, "Neutral"),
    WEAKLY_POSITIVE(1, "Weakly Positive"),
    SLIGHTLY_POSITIVE(2, "Slightly Positive"),
    MODERATELY_POSITIVE(3, "Moderately Positive"),
    POSITIVE(4, "Positive"),
    VERY_POSITIVE(5, "Very Positive");

    private final int score;
    private final String description;

    SentimentScore(int score, String description) {
        this.score = score;
        this.description = description;
    }

    public static SentimentScore fromScore(int score) {
        for (SentimentScore sentiment : values()) {
            if (sentiment.getScore() == score) {
                return sentiment;
            }
        }
        throw new IllegalArgumentException("Invalid sentiment score: " + score);
    }

    public int getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }
}