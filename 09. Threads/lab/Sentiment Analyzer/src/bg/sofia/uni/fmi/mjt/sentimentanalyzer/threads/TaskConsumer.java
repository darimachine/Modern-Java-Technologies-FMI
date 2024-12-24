package bg.sofia.uni.fmi.mjt.sentimentanalyzer.threads;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.TaskQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.io.BufferedReader;
import java.util.Map;

public class TaskConsumer implements Runnable {
    private final TaskQueue taskQueue;
    private final Map<String, SentimentScore> results;
    private final Map<String, SentimentScore> sentimentLexicon;
    private final TextTokenizer tokenizer;

    public TaskConsumer(TaskQueue taskQueue, Map<String, SentimentScore> results,
                        Map<String, SentimentScore> sentimentLexicon, TextTokenizer tokenizer) {
        this.taskQueue = taskQueue;
        this.results = results;
        this.sentimentLexicon = sentimentLexicon;
        this.tokenizer = tokenizer;
    }

    @Override
    public void run() {
        while (true) {
            AnalyzerInput task = taskQueue.take(); // Fetch a task
            if (task == null && taskQueue.isProducerFinished()) {
                break; // Exit if the queue is empty and the producer is done
            }
            if (task != null) {
                String text = new BufferedReader(task.inputReader()).lines().reduce("", String::concat);
                int score = tokenizer.tokenize(text).stream()
                    .filter(sentimentLexicon::containsKey)
                    .mapToInt(word -> sentimentLexicon.get(word).getScore())
                    .sum();

                results.put(task.inputID(), SentimentScore.fromScore(score));
            }
        }

    }
}
