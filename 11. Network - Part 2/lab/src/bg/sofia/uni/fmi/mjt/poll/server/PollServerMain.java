package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;

public class PollServerMain {
    public static void main(String[] args) {
        final int port = 7777;
        PollServer server = new PollServer(port, new InMemoryPollRepository());

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop)); // Ensures server stops on shutdown

        server.start();
    }
}
