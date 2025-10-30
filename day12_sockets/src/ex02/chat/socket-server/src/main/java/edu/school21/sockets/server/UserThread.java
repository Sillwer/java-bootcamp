package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Timestamp;

class UserThread extends Thread {
    private User user;
    private Chatroom chatroom;
    private ChatroomThread chatroomThread;
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
            MenuItem selectedMenuItem = menu(new MenuItem[]{
                    MenuItem.SIGH_IN,
                    MenuItem.SIGN_UP,
                    MenuItem.EXIT
            });
            processMenuItem(selectedMenuItem);

            if (isInterrupted() || user == null) {
                return;
            }

            selectedMenuItem = menu(new MenuItem[]{
                    MenuItem.ROOM_CHOOSE,
                    MenuItem.ROOM_CREATE,
                    MenuItem.EXIT
            });
            processMenuItem(selectedMenuItem);

            if (isInterrupted() || chatroomThread == null) {
                return;
            }

            while (!isInterrupted()) {
                if (!socketScanner.hasNextLine()) {
                    Thread.sleep(100);
                    continue;
                }

                String userMessage = socketScanner.nextLine().trim();
                if (userMessage.equalsIgnoreCase("exit")) {
                    interrupt();
                    return;
                }

                Message message = new Message(
                        null,
                        userMessage,
                        chatroom.getId(),
                        user.getId(),
                        new Timestamp(System.currentTimeMillis())
                );
                chatroomThread.addMessage(user.getUserName(), message);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            interrupt();
        }
    }

    private MenuItem menu(MenuItem[] menuItems) {
        String menuList = "\n" + MenuItem.getNumberedMenuList(menuItems);
        MenuItem selectedMenuItem = null;

        while (!isInterrupted()) {
            socketPrinter.println(menuList);
            String userInput = null;
            try {
                if (!socketScanner.hasNextLine()) {
                    continue;
                }
                userInput = socketScanner.nextLine().trim();
                if (userInput.equalsIgnoreCase("exit")) {
                    selectedMenuItem = MenuItem.EXIT;
                    break;
                }
                int selectedMenuIndex = Integer.parseInt(userInput);
                if (selectedMenuIndex < 1 || selectedMenuIndex > menuItems.length) {
                    throw new RuntimeException();
                }
                selectedMenuItem = menuItems[selectedMenuIndex - 1];
                break;
            } catch (Exception e) {
                socketPrinter.println("Unexpected menu input (" + userInput + ")");
                continue;
            }
        }

        return selectedMenuItem;
    }

    private void processMenuItem(MenuItem menuItem) {
        switch (menuItem) {
            case SIGN_UP:
                registration();
                break;
            case SIGH_IN:
                authentication();
                break;
            case ROOM_CREATE:
                roomCreate();
                break;
            case ROOM_CHOOSE:
                roomChoose();
                break;
            case EXIT:
                interrupt();
            default:
                break;
        }
    }

    private void registration() {
        try {
            socketPrinter.println("\n### Registration procedure ###");
            socketPrinter.println("Enter username:");
            String userName = socketScanner.nextLine().trim();
            socketPrinter.println("Enter password:");
            String password = socketScanner.nextLine().trim();
            User user = new User(null, userName, password);

            Optional<User> registeredUser = Server.signUp(user);
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
    }

    private void authentication() {
        socketPrinter.println("\n### Authentication procedure ###");
        socketPrinter.println("Enter username:");
        String userName = socketScanner.nextLine().trim();
        socketPrinter.println("Enter password:");
        String password = socketScanner.nextLine().trim();
        User user = new User(null, userName, password);

        try {
            Optional<User> authenticatedUser = Server.signIn(user);
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
    }

    private void roomCreate() {
        socketPrinter.println("\n### Room creating procedure ###");
        socketPrinter.println("Enter new room name:");
        String chatroomName = socketScanner.nextLine().trim();
        chatroom = new Chatroom(null, chatroomName, user.getId());
        Server.createRoom(chatroom);
        enterRoom();
    }

    private void roomChoose() {
        List<Chatroom> chatroomList = Server.getChatRooms();
        socketPrinter.println("\nChat rooms:");
        for (int i = 0; i < chatroomList.size(); i++) {
            socketPrinter.println("\t" + (i + 1) + ") " + chatroomList.get(i).getName());
        }
        socketPrinter.println("Enter chat room number:");

        while (!isInterrupted()) {
            try {
                String input = socketScanner.nextLine().trim();
                int i = Integer.parseInt(input) - 1;
                chatroom = chatroomList.get(i);
                enterRoom();
                break;
            } catch (Exception e) {
                socketPrinter.println("Unexpected input. Try agan");
            }
        }
    }

    private void enterRoom() {
        chatroomThread = Server.userEnterRoom(chatroom, socketPrinter);
        socketPrinter.println("Logged to \"" + chatroom.getName() + "\" chatroom");

        List<String> messageList = Server.getChatRoomMessages(chatroom, 30);
        messageList.forEach(socketPrinter::println);
    }

    @Override
    public void interrupt() {
        closeResources();
//        userThreads.remove(this);
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
            System.out.println("UserThread closing exception: " + e);
        }
    }
}
