package edu.school21.sockets.server;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

public class Server implements AutoCloseable {
    private UsersService usersService;
    private MessageService messageService;
    private ServerSocket serverSocket;
    private List<UserThread> userThreads = new ArrayList<>();

    private enum MENU_ITEM {
        SIGH_IN, SIGN_UP, EXIT;

        public static String getNumberedMenuList(MENU_ITEM[] itemsArr) {
            StringBuilder menuList = new StringBuilder();
            for (int i = 0; i < itemsArr.length; i++) {
                menuList.append(String.format("%d) %s", i + 1, itemsArr[i]));
                if (i < itemsArr.length - 1) {
                    menuList.append("\n");
                }
            }
            return menuList.toString();
        }

        @Override
        public String toString() {
            switch (this) {
                case SIGH_IN:
                    return "Sign in";
                case SIGN_UP:
                    return "Sign up";
                case EXIT:
                    return "Exit";
                default:
                    return "_";
            }
        }
    }

    public Server(UsersService usersService, MessageService messageService, int port) throws IOException {
        this.usersService = usersService;
        this.messageService = messageService;
        this.serverSocket = new ServerSocket(port);
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
                System.out.println(e);
            }
        } while (true);
    }

    private Optional<User> signUp(PrintWriter socketPrinter, Scanner socketScanner) throws Exception {
        socketPrinter.println("\n### Registration procedure ###");
        socketPrinter.println("Enter username:");
        String userName = socketScanner.nextLine().trim();
        socketPrinter.println("Enter password:");
        String password = socketScanner.nextLine().trim();

        User user = new User(null, userName, password);
        if (usersService.signUp(user)) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Optional<User> signIn(PrintWriter socketPrinter, Scanner socketScanner) {
        socketPrinter.println("\n### Authentication procedure ###");
        socketPrinter.println("Enter username:");
        String userName = socketScanner.nextLine().trim();
        socketPrinter.println("Enter password:");
        String password = socketScanner.nextLine().trim();

        User user = new User(null, userName, password);
        if (usersService.signIn(user)) {
            return Optional.of(user);
        } else {
            throw new RuntimeException("User " + userName + " not found, or wrong password");
        }
    }

    private void massiveMailing(String message) {
        for (UserThread userThread : userThreads) {
            if (userThread.isAlive()) {
                userThread.sendMessage(message);
            }
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
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
            throw new RuntimeException(e);
        }
    }

    private class UserThread extends Thread {
        private User user;
        private final Socket socket;
        private final PrintWriter socketPrinter;
        private final Scanner socketScanner;

        public UserThread(Socket socket) {
            this.socket = socket;
            try {
                socketPrinter = new PrintWriter(socket.getOutputStream(), true);
                socketScanner = new Scanner(socket.getInputStream());
            } catch (Exception e) {
                closeResources();
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            socketPrinter.println("Hello from Server!");

            try {
                MENU_ITEM menuItem = startMenu();

                switch (menuItem) {
                    case SIGN_UP:
                        try {
                            Optional<User> registeredUser = signUp(socketPrinter, socketScanner);
                            if (registeredUser.isPresent()) {
                                this.user = registeredUser.get();
                                System.out.println("Registered new user: " + this.user);
                                socketPrinter.println("Successful!");
                            }
                        } catch (Exception e) {
                            socketPrinter.println("Problem: " + e.getMessage());
                            socketPrinter.println("exit");
                            interrupt();
                        }
                        break;
                    case SIGH_IN:
                        try {
                            Optional<User> authenticatedUser = signIn(socketPrinter, socketScanner);
                            if (authenticatedUser.isPresent()) {
                                this.user = authenticatedUser.get();
                                System.out.println("Authenticated user: " + this.user);
                                socketPrinter.println("Successful!");
                            }
                        } catch (Exception e) {
                            socketPrinter.println("Problem: " + e.getMessage());
                            socketPrinter.println("exit");
                            interrupt();
                        }
                        break;
                    case EXIT:
                        interrupt();
                    default:
                        break;
                }

                if (isInterrupted() || user == null) {
                    return;
                }

                socketPrinter.println("Hello " + user.getUserName());
                while (!isInterrupted()) {
                    if (!socketScanner.hasNextLine()) {
                        Thread.sleep(100);
                        continue;
                    }

                    String userMessage = socketScanner.nextLine().trim();
                    Message message = new Message(
                            null,
                            userMessage,
                            null,
                            user.getId(),
                            new Timestamp(System.currentTimeMillis())
                    );
                    messageService.save(message);
                    massiveMailing(user.getUserName() + ": " + userMessage);

                    if (userMessage.equalsIgnoreCase("exit")) {
                        interrupt();
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                interrupt();
            }
        }

        private MENU_ITEM startMenu() {
            MENU_ITEM[] menuItems = {
                    MENU_ITEM.SIGH_IN,
                    MENU_ITEM.SIGN_UP,
                    MENU_ITEM.EXIT
            };
            String menuList = "\n" + MENU_ITEM.getNumberedMenuList(menuItems);

            MENU_ITEM selectedMenuItem = null;

            while (!isInterrupted()) {
                socketPrinter.println(menuList);

                String userInput = null;
                try {
                    userInput = socketScanner.nextLine().trim();
                    if (userInput.equalsIgnoreCase("exit")) {
                        selectedMenuItem = MENU_ITEM.EXIT;
                        break;
                    }
                    int selectedMenuIndex = Integer.parseInt(userInput);
                    if (selectedMenuIndex < 1 || selectedMenuIndex > menuItems.length) {
                        throw new RuntimeException();
                    }
                    selectedMenuItem = menuItems[selectedMenuIndex - 1];
                    break;
                } catch (NumberFormatException e) {
                    socketPrinter.println("Unexpected menu number (" + userInput + ")");
                    continue;
                }
            }

            return selectedMenuItem;
        }

        public void sendMessage(String message) {
            socketPrinter.println(message);
        }

        @Override
        public void interrupt() {
            closeResources();
            userThreads.remove(this);
            super.interrupt();
        }

        private void closeResources() {
            try {
                if (socketPrinter != null) {
                    socketPrinter.close();
                }
                if (socketScanner != null) {
                    socket.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
