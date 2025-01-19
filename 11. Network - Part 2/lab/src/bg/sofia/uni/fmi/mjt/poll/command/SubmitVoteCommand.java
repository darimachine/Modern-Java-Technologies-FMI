package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.HashMap;
import java.util.Map;

public class SubmitVoteCommand implements CommandHandler {
    private final PollRepository repository;
    private final String[] args;

    public SubmitVoteCommand(PollRepository repository, String[] args) {
        this.repository = repository;
        this.args = args;
    }

    @Override
    public String execute() {
        if (args.length != 2) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: submit-vote <poll-id> <option>\"}";
        }
        int pollId;
        try {
            pollId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Poll ID must be a valid integer.\"}";
        }

        String option = args[1];
        Poll poll = repository.getPoll(pollId);

        if (poll == null) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Poll with ID %d does not exist.\"}", pollId);
        }

        Map<String, Integer> options = new HashMap<>(poll.options());
        if (!options.containsKey(option)) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Invalid option: %s\"}", option);
        }

        options.put(option, options.get(option) + 1);
        Poll updatedPoll = new Poll(poll.question(), options);
        repository.getAllPolls().put(pollId, updatedPoll);

        return String.format("{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: %s\"}", option);
    }
}
