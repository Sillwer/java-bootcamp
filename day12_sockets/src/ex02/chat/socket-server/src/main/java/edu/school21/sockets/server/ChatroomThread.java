package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.services.MessageService;
import javafx.util.Pair;

import java.io.PrintWriter;
import java.util.*;

public class ChatroomThread extends Thread {
    private static MessageService messageService;
    private final Chatroom chatroom;
    private final List<PrintWriter> socketPrinterList = new ArrayList<>();
    private final Queue<Pair<String, Message>> messageQueue = new PriorityQueue<>();

    ChatroomThread(MessageService messageService, Chatroom chatroom) {
        this.chatroom = chatroom;
        ChatroomThread.messageService = messageService;
    }

    void enterUser(PrintWriter socketPrinter) {
        socketPrinterList.add(socketPrinter);
    }

    void addMessage(String userName, Message message) {
        try {
            synchronized (messageQueue) {
                messageService.save(message);
                messageQueue.add(new Pair<>(userName, message));
            }
        } catch (Exception e) {
            System.out.println("Message [" + message + "] saving in ChatroomThread error: " + e);
        }

    }

    @Override
    public void run() {
        while (!interrupted()) {
            if (messageQueue.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            Pair<String, Message> msg;
            synchronized (messageQueue) {
                msg = messageQueue.poll();
            }

            for (PrintWriter socketPrinter : socketPrinterList) {
                try {
                    socketPrinter.println(msg.getKey() + ": " + msg.getValue().getContent());
                } catch (Exception e) {
                    System.out.println("Message [" + msg + "] output in ChatroomThread error: " + e);
                }
            }
        }
    }
}
