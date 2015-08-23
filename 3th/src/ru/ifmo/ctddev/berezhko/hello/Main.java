package ru.ifmo.ctddev.berezhko.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

public class Main {
    public static void main(String[] args) {
        try (HelloServer server = new HelloUDPServer();) {
            server.start(8888, 10);
            for (int i = 0; i < 10; i++) {
                new HelloUDPClient().start("127.0.0.1", 8888, "hrhhrjffhаорааарраееннн" + i + "_", 10, 10);
            }
        }
    }
}
