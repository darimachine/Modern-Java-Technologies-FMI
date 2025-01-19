package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPollRepository implements PollRepository {

    private int currentId;
    private final Map<Integer, Poll> polls;

    public InMemoryPollRepository() {
        this.currentId = 0;
        this.polls = new ConcurrentHashMap<>();
    }

    @Override
    public int addPoll(Poll poll) {
        int id = ++currentId;
        polls.put(id, poll);
        return id;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return polls;
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
        currentId = 0;
    }
}
