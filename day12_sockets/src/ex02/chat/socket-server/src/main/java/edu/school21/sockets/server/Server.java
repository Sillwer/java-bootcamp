package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements AutoCloseable {
    private static UsersService usersService;
    private static MessageService messageService;
    private static ChatroomService chatroomService;
    private static ServerSocket serverSocket;
    private static final List<UserThread> userThreads = new ArrayList<>();
    private static final HashMap<Long, ChatroomThread> chatroomThreads = new HashMap<>();

    public Server(UsersService usersService, MessageService messageService, ChatroomService chatroomService, int port) throws IOException {
        Server.usersService = usersService;
        Server.chatroomService = chatroomService;
        Server.messageService = messageService;
        Server.serverSocket = new ServerSocket(port);
    }

    public void run() {
        System.out.println("** Server.run()");

        do {
            try {
                // TODO: обработка закрытия сокета (при завершении клиентского потока)
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket);
                userThreads.add(userThread);
                userThread.start();
                System.out.println("new user connected | " + userThread);
            } catch (Exception e) {
                System.out.println("User connecting exception:" + e);
            }
        } while (true);
    }

    static Optional<User> signUp(User user) throws Exception {
        if (usersService.signUp(user)) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    static Optional<User> signIn(User user) {
        if (usersService.signIn(user)) {
            return Optional.of(user);
        } else {
            throw new RuntimeException("User " + user.getUserName() + " not found, or wrong password");
        }
    }

    static void createRoom(Chatroom chatroom) {
        chatroomService.createRoom(chatroom);
        System.out.println("Created new chatroom: " + chatroom);
    }

    static ChatroomThread userEnterRoom(Chatroom chatroom, PrintWriter socketPrinter) {
        ChatroomThread chatroomThread = null;

        if (chatroomThreads.containsKey(chatroom.getId())) {
            chatroomThread = chatroomThreads.get(chatroom.getId());
        } else {
            chatroomThread = new ChatroomThread(messageService, chatroom);
            chatroomThread.start();
            chatroomThreads.put(chatroom.getId(), chatroomThread);
        }

        chatroomThread.enterUser(socketPrinter);
        return chatroomThread;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    static List<Chatroom> getChatRooms() {
        return chatroomService.findAll();
    }

    static List<String> getChatRoomMessages(Chatroom chatroom, Integer count) {
        List<Message> messageList = messageService.findByChatRoom(chatroom.getId());
        if (count == null || count < 1) {
            count = 30;
        }
        count = Math.min(messageList.size(), count);
        messageList = messageList.subList(messageList.size() - count, messageList.size());

        List<String> messages = new ArrayList<>();
        for (Message m : messageList) {
            try {
                messages.add(usersService.findById(m.getAuthorId()).get().getUserName() + ": " + m.getContent());
            } catch (Exception e) {
                System.out.println("getChatRoomMessages: " + e);
            }
        }
        return messages;
    }

    @Override
    public void close() {
        try {
            serverSocket.close();
            for (UserThread userThread : userThreads) {
                if (!userThread.isInterrupted()) {
                    userThread.interrupt();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Server closing exception: " + e);
        }
    }
}
