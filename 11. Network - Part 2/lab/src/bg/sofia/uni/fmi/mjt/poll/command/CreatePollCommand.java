package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.HashMap;
import java.util.Map;

public class CreatePollCommand implements CommandHandler {
    private final PollRepository repository;
    private final String[] args;
    public CreatePollCommand(PollRepository repository, String[] args) {
        this.repository = repository;
        this.args = args;
    }

    @Override
    public String execute() {
        if (args.length <= 2) {
            return "{\"status\":\"ERROR\",\"message\":" +
                "\"Usage: create-poll <question> <option1> <option2> [... <optionN>]\"}";
        }

        String question = args[0];
        Map<String, Integer> options = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (options.containsKey(args[i])) {
                return String.format("{\"status\":\"ERROR\",\"message\":\"Duplicate option: %s\"}", args[i]);
            }
            options.put(args[i], 0);
        }

        Poll poll = new Poll(question, options);
        int pollId = repository.addPoll(poll);

        return String.format("{\"status\":\"OK\",\"message\":\"Poll %d created successfully.\"}", pollId);
    }
}
