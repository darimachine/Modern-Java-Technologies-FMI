package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Logger;

public class TaskQueue {
    private final Queue<AnalyzerInput> queue = new ArrayDeque<>();
    private boolean producerFinished = false;
    private static final Logger LOGGER = Logger.getLogger(TaskQueue.class.getName());

    public synchronized void put(AnalyzerInput task) {
        //System.out.println(Thread.currentThread().getName() + " is adding a task to the queue");
        queue.add(task);
        LOGGER.info(Thread.currentThread().getName() + " added a task");
        notifyAll(); // Notify waiting consumers that a task is available
    }

    public synchronized AnalyzerInput take() {
        try {
            while (queue.isEmpty() && !producerFinished) {

                LOGGER.info(Thread.currentThread().getName() + " is waiting...");
                this.wait(); // Wait until a task is added or the producer is finished
            }
            AnalyzerInput task = queue.poll();
            LOGGER.info(Thread.currentThread().getName() + " retrieved a task " + task);

            return task; // Return the next task (null if empty and finished)
        } catch (InterruptedException ex) {
            throw new SentimentAnalysisException("Error while taking on thread" + ex);
        }

    }

    public synchronized void setProducerFinished() {
        producerFinished = true;
        LOGGER.info(Thread.currentThread().getName() + " set producer finished");
        notifyAll(); // Notify waiting consumers that no more tasks will be added
    }

    public synchronized boolean isProducerFinished() {
        return producerFinished && queue.isEmpty();
    }
}
