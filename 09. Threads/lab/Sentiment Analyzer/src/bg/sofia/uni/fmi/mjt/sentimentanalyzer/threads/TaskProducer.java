package bg.sofia.uni.fmi.mjt.sentimentanalyzer.threads;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.TaskQueue;

import java.util.List;

public class TaskProducer implements Runnable {
    private final TaskQueue taskQueue;
    private final List<AnalyzerInput> inputs;

    public TaskProducer(TaskQueue taskQueue, List<AnalyzerInput> inputs) {
        this.taskQueue = taskQueue;
        this.inputs = inputs;
    }

    @Override
    public void run() {
        for (AnalyzerInput input : inputs) {
            taskQueue.put(input); // Add tasks to the queue
        }
        taskQueue.setProducerFinished(); // Signal that all tasks have been produced
    }
}
