package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Map;

public class ListPollsCommand implements CommandHandler {

    private final PollRepository repository;
    private final String[] args;

    public ListPollsCommand(PollRepository repository, String[] args) {
        this.repository = repository;
        this.args = args;
    }

    @Override
    public String execute() {
        Map<Integer, Poll> polls = repository.getAllPolls();
        if (polls.isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\"status\":\"OK\",\"polls\":{");
        for (var entry : polls.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":{\"question\":\"")
                .append(entry.getValue().question()).append("\",\"options\":")
                .append(entry.getValue().options()).append("},");
        }
        sb.setLength(sb.length() - 1); // Премахваме последната запетая
        sb.append("}}");
        return sb.toString();
    }
}
