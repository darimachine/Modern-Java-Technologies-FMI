package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.threads.TaskConsumer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.threads.TaskProducer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {
    private final int workersCount;

    private final Map<String, SentimentScore> sentimentLexicon;
    private final TaskQueue taskQueue;
    private final Map<String, SentimentScore> results;
    private final TextTokenizer tokenizer;

    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.sentimentLexicon = sentimentLexicon;
        this.tokenizer = new TextTokenizer(stopWords);
        this.results = new ConcurrentHashMap<>();
        this.taskQueue = new TaskQueue();
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... inputs) {
        // Start producer
        Thread producer = new Thread(new TaskProducer(taskQueue, List.of(inputs)));
        producer.start();
        // Start consumers
        List<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < workersCount; i++) {
            Thread consumer = new Thread(new TaskConsumer(taskQueue, results, sentimentLexicon, tokenizer));
            consumer.start();
            consumers.add(consumer);
        }
        // Wait for producer to finish
        try {
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Producer thread interrupted", e);
        }
        // Wait for consumers to finish
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Consumer thread interrupted", e);
            }
        }
        return results;
    }
}
