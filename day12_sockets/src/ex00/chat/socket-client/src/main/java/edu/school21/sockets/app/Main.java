package edu.school21.sockets.app;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    static int port = 8081;
    static boolean running = true;

    public static void main(String[] args) {
        Optional<String> portArg = Arrays.stream(args).filter(s -> s.contains("port")).findFirst();
        if (portArg.isPresent()) {
            String[] argParts = portArg.get().split("=");
            if (argParts.length == 2) {
                port = Integer.parseInt(argParts[1]);
            }
        }

        System.out.println("Client connecting to localhost by port " + port + " ...");
        try (Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), port)) {
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Scanner socketScanner = new Scanner(socket.getInputStream());
                    Scanner inputScanner = new Scanner(System.in)) {
                Thread readerThread = new Thread(() -> {
                    System.out.println("Client listening messages");
                    try {
                        while (running) {
                            String messageFromServer = socketScanner.nextLine();
                            if (messageFromServer == null) {
                                throw new Exception("server closed connection");
                            } else {
                                messageFromServer = messageFromServer.trim();
                            }
                            System.out.println(messageFromServer);
                            if (messageFromServer.equalsIgnoreCase("exit")) {
                                running = false;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        running = false;
                    }
                });
                readerThread.start();

                Thread.sleep(500);
                while (running && !socket.isClosed()) {
                    try {
                        System.out.print("> ");
                        String message = inputScanner.nextLine();
                        out.println(message);

                        if (message.trim().equalsIgnoreCase("exit")) {
                            running = false;
                        }
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println(e);
                        running = false;
                    }
                }
                readerThread.join(1000);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            running = false;
        }
    }
}