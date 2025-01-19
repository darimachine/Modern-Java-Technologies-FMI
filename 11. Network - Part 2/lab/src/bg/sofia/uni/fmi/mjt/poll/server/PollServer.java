package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.poll.command.CommandHandler;

import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class PollServer {

    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";
    private boolean isServerWorking;
    private static final int PORT_MIN = 1024;
    private static final int PORT_PROBA = 7777;

    private ByteBuffer buffer;
    private Selector selector;
    int port;
    PollRepository pollRepository;

    public PollServer(int port, PollRepository pollRepository) {
        if (port <= PORT_MIN && pollRepository == null) {
            this.port = PORT_PROBA;
            this.pollRepository = new InMemoryPollRepository();
        } else {
            this.port = port;
            this.pollRepository = pollRepository;
        }

    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }
                    handleClientRequests();
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    private void handleClientRequests() throws IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                String clientInput = getClientInput(clientChannel);
                System.out.println(clientInput);
                if (clientInput == null) {
                    continue;
                }
                CommandHandler command = CommandCreator.createCommand(clientInput, pollRepository);
                String response = command.execute();
                writeClientOutput(clientChannel, response);
            } else if (key.isAcceptable()) {
                accept(selector, key);
            }
            keyIterator.remove();

        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel serverSocketChannel,
                                              Selector selector) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(HOST, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        output += "\n"; // Add newline to mark the end of the message
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
        System.out.println("Sent response: " + output);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();
        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
            clientChannel.close();
            return null;
        }
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}