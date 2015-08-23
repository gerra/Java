package ru.ifmo.ctddev.berezhko.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Class implementing UDP Server. It receives request _request_ to the specific port
 * using specific threads count and then it sends message "Hello, _request_"
 * to the sender of _request_
 * Instance of the class can be created from command line with arguments:
 * <p>
 * args[0] - port <p>
 * args[1] - threads count
 *
 * @see info.kgeorgiy.java.advanced.hello.HelloServer
 * @see #main
 * @see #start
 */
public class HelloUDPServer implements HelloServer {
    private static final int BUFFER_SIZE = 1024;
    private static final String USAGE = "Usage: port number_of_threads";
    private final ConcurrentLinkedQueue<DatagramSocket> receivingSockets = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ExecutorService> services = new ConcurrentLinkedQueue<>();

    /**
     * Create instance of {@link ru.ifmo.ctddev.berezhko.hello.HelloUDPServer} and start receiving requests
     * on the specific port using specific number of threads
     *
     * @param args port and threads count
     * @see #start
     */
    public static void main(String[] args) {
        System.err.println(args.length);
        Arrays.stream(args).forEach(System.err::println);
        if (args == null || args.length < 2 || Arrays.stream(args).anyMatch(Predicate.isEqual(null))) {
            System.out.println(USAGE);
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);
            if (port < 0 || port > 0xFFFF || threads < 1) {
                throw new NumberFormatException();
            }
            new HelloUDPServer().start(port, threads);
        } catch (NumberFormatException ignored) {
            System.out.println(USAGE);
        }
    }

    /**
     * Start to receive requests to the specific port using specific threads count
     *
     * @param port specific port
     * @param threads specific threads count
     *
     * @see java.net.DatagramSocket
     */
    @Override
    public void start(int port, int threads) {
        if (port < 0 || port > 0xFFFF) {
            System.err.println("Port is not in interval 0..65535");
            return;
        }
        if (threads < 1) {
            System.err.println("Number of threads must be greater than 0");
            return;
        }
        ExecutorService service = Executors.newFixedThreadPool(threads);
        services.add(service);
        try {
            DatagramSocket receivingSocket = new DatagramSocket(port);
            receivingSockets.add(receivingSocket);
            for (int i = 0; i < threads; ++i) {
                service.submit(() -> {
                    try (DatagramSocket sendingSocket = new DatagramSocket()) {
                        DatagramPacket receivingPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
                        while (!Thread.interrupted() && !receivingSocket.isClosed()) {
                            receivingSocket.receive(receivingPacket);
                            String received = new String(receivingPacket.getData(), 0, receivingPacket.getLength());
                            String sending = "Hello, " + received;
                            sendingSocket.send(new DatagramPacket(sending.getBytes(), sending.getBytes().length,
                                    receivingPacket.getAddress(), receivingPacket.getPort()));
                        }
                    } catch (SocketException e) {
                        System.err.println("Socket is closed " + port);
                    } catch (IOException e) {
                        System.err.println("Error in sending packet");
                    }
                });
            }
        } catch (SocketException e) {
            System.err.println("Socket = " + port + "is already in use!");
        }
    }

    /**
     * Method to close server
     *
     * @see java.util.concurrent.ExecutorService
     * @see java.net.DatagramSocket
     */
    @Override
    public synchronized void close() {
        synchronized (services) {
            services.parallelStream().forEach(service -> {
                service.shutdown();
                try {
                    service.awaitTermination(1, TimeUnit.SECONDS);
                    service.shutdownNow();
                } catch (InterruptedException ignored) {
                }
            });
            services.clear();
            synchronized (receivingSockets) {
                receivingSockets.parallelStream().forEach(DatagramSocket::close);
                receivingSockets.clear();
            }
        }
    }
}
