package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Map;

public interface PollRepository {

    /**
     * Adds a new poll to the repository.
     *
     * @param poll the {@link Poll} object to be added to the repository.
     * @return the ID of the new poll
     */
    int addPoll(Poll poll);

    /**
     * Retrieves a poll from the repository by its unique ID.
     *
     * @param pollId the unique identifier of the poll to retrieve.
     * @return the {@link Poll} object associated with the given ID,
     * or {@code null} if no such poll exists.
     */
    Poll getPoll(int pollId);

    /**
     * Retrieves all polls stored in the repository.
     *
     * @return a {@link Map} containing all polls, where the key is the poll ID
     * and the value is the {@link Poll} object.
     */
    Map<Integer, Poll> getAllPolls();

    /**
     * Clears all polls from the repository.
     * <p>
     * This method removes all stored {@link Poll} objects, leaving the repository empty.
     */
    void clearAllPolls();

}