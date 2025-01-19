package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

public class CommandCreator {
    public static CommandHandler createCommand(String clientInput, PollRepository repository) {
        if (clientInput == null || clientInput.isBlank()) {
            return () -> "{\"status\":\"ERROR\",\"message\":\"Invalid input\"}";
        }
        String[] tokens = clientInput.trim().split("\\s+", 2); // Split command name and arguments
        String commandName = tokens[0];
        String[] args;
        if (tokens.length > 1) {
            args = tokens[1].split("\\s+");
        } else {
            args = new String[0];
        }

        switch (commandName) {
            case "create-poll":
                return new CreatePollCommand(repository, args);
            case "list-polls":
                return new ListPollsCommand(repository, args);
            case "submit-vote":
                return new SubmitVoteCommand(repository, args);
            default:
                return () -> String.format("{\"status\":\"ERROR\",\"message\":\"Unknown command: %s\"}",
                    commandName);
        }
    }
}
