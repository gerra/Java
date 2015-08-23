package ru.ifmo.ctddev.berezhko.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Class implementing {@link info.kgeorgiy.java.advanced.hello.HelloClient} for sending and receiving specific requests count
 * using specific threads count built in format: prefix+threadId_+requestId
 * Instance of the class can be created from command line with arguments: <p>
 * name or ip of server, port to send requests, prefix of requests, requests per thread count, threads count
 * @see #main
 * @see #start
 */
public class HelloUDPClient implements HelloClient {
    private static final String USAGE = "Usage: url|ip-address port request number_of_threads number_of_requests_per_thread";
    private static final long TERMINATION_TIME_OUT = 10;
    private static final int SOCKET_TIMEOUT = 50;

    /**
     * Create instance of {@link ru.ifmo.ctddev.berezhko.hello.HelloUDPClient} and start to send and receive requests
     *
     * @param args name or ip of server, port to send requests, prefix of requests, requests per thread count, threads count
     * @see #start
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0 || args.length > 5 ||
                Arrays.stream(args).anyMatch(Predicate.isEqual(null))) {
            System.err.println(USAGE);
            return;
        }
        try {
            String url = args[0];
            int port = Integer.parseInt(args[1]);
            String request = args[2];
            int numberOfThreads = Integer.parseInt(args[3]);
            int numberOfRequestsPerThread = Integer.parseInt(args[4]);
            if (port < 0 || port > 0xFFFF || numberOfThreads < 1 || numberOfRequestsPerThread < 1) {
                throw new NumberFormatException();
            }
            new HelloUDPClient().start(url, port, request, numberOfThreads, numberOfRequestsPerThread);
        } catch (NumberFormatException ignored) {
            System.err.println(USAGE);
        }
    }

    /**
     * Start sending specific count of requests to specific port and receiving replies from specific server
     * Requests are built in this format: prefix+threadId_+requestId
     *
     * @param url name or ip of server
     * @param port port to send request
     * @param prefix prefix of requests
     * @param requests requests per thread count
     * @param threads thread count to build requests
     */
    @Override
    public void start(String url, int port, String prefix, int requests, int threads) {
        if (port < 0 || port > 0xFFFF) {
            System.err.println("Port id is incorrect, has to be between 0 and 65535");
            return;
        }
        if (threads < 1) {
            System.err.println("Number of threads is non-positive");
            return;
        }
        ExecutorService service = Executors.newFixedThreadPool(threads);
        try {
            InetAddress address = InetAddress.getByName(url);
            for (int i = 0; i < threads; ++i) {
                final int threadId = i;
                service.submit(() -> {
                    try (DatagramSocket socket = new DatagramSocket()) {
                        socket.setSoTimeout(SOCKET_TIMEOUT);
                        for (int requestId = 0; requestId < requests; requestId++) {
                            String request = prefix + threadId + "_" + requestId;
                            int len = request.getBytes().length;
                            DatagramPacket sendingPacket =
                                    new DatagramPacket(request.getBytes(), len, address, port);
                            DatagramPacket receivedPacket = new DatagramPacket(new byte[len + 10], len + 10);
                            String required = "Hello, " + request;
                            String received = "";
                            while (!required.equals(received)) {
                                try {
                                    System.out.println("Sending request = " + request);
                                    socket.send(sendingPacket);

                                    socket.receive(receivedPacket);
                                    received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                                    System.out.println("Received = " + received);
                                } catch (IOException ignored) {
                                    System.err.println("Error in sending packet");
                                }
                            }
                        }
                    } catch (SocketException e) {
                        System.err.println("Socket is closed");
                    }
                });
            }

            service.shutdownNow();
            service.awaitTermination(TERMINATION_TIME_OUT, TimeUnit.SECONDS);
        } catch (UnknownHostException ignored) {
            System.err.println("Host can't be identified: " + url);
        } catch (InterruptedException ignored) {
        }
    }

}
