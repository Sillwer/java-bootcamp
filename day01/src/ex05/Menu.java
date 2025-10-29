package ex05;

import java.io.StringReader;
import java.util.Scanner;
import java.util.UUID;

public class Menu {
    boolean devMode;
    Scanner scanner;
    TransactionsService service;
    String[] menuItem;

    Menu(boolean devMode, Scanner scanner, TransactionsService transactionsService) {
        this.devMode = devMode;
        this.scanner = scanner;
        this.service = transactionsService;

        if (devMode) {
            menuItem = new String[]{
                    "Add a user",
                    "View user balances",
                    "Perform a transfer",
                    "View all transactions for a specific user",
                    "DEV – remove a transfer by ID",
                    "DEV – check transfer validity",
                    "Show users list",
                    "Finish execution"
            };
        } else {
            menuItem = new String[]{
                    "Add a user",
                    "View user balances",
                    "Perform a transfer",
                    "View all transactions for a specific user",
                    "Show users list",
                    "Finish execution"
            };
        }
    }

    void printMenu() {
        System.out.println("\n---------------------------------------------------------");
        for (int i = 0; i < menuItem.length; i++) {
            System.out.printf("%d. %s\n", i + 1, menuItem[i]);
        }
    }

    int getInputMenuItem() {
        int item = -1;
        while (true) {
            System.out.print("-> ");
            try {
                String input = scanner.nextLine();
                item = Integer.parseInt(input);
            } catch (Exception ignored) {
            }

            if (item < 1 || item > menuItem.length) {
                System.out.println("Wrong menu item");
            } else {
                return item;
            }
        }
    }

    boolean perform(int item) {
        if (devMode) {
            switch (item) {
                case 1:
                    addUser();
                    break;
                case 2:
                    getBalance();
                    break;
                case 3:
                    performTransfer();
                    break;
                case 4:
                    printUserTransactions();
                    break;
                ////////////////////////////////////
                case 5:
                    removeTransfer();
                    break;
                case 6:
                    checkTransferValidity();
                    break;
                case 7:
                    service.printUsersList();
                    break;
                case 8:
                    System.out.println("Goodbye");
                    return false;
                default:
                    System.out.println("Wrong menu item");
            }
        } else {
            switch (item) {
                case 1:
                    addUser();
                    break;
                case 2:
                    getBalance();
                    break;
                case 3:
                    performTransfer();
                    break;
                case 4:
                    printUserTransactions();
                    break;
                case 5:
                    service.printUsersList();
                    break;
                case 6:
                    System.out.println("Goodbye");
                    return false;
                default:
                    System.out.println("Wrong menu item");
            }
        }

        return true;
    }

    void addUser() {
        System.out.println("Enter a user name and a balance (example: Max 700)");

        while (true) {
            System.out.print("-> ");
            try {
                String input = scanner.nextLine();
                Scanner sc = new Scanner(input);

                String name = sc.next();
                int balance = sc.nextInt();
                sc.close();

                if (service.addUser(name, balance)) {
                    System.out.printf("User [%s] with balance [%d] successfully added\n", name, balance);
                    break;
                }
            } catch (Exception ignore) {
            }
            System.out.println("Wrong input. Repeat please");
        }
    }

    void getBalance() {
        if (service.getNumberOfUsers() == 0) {
            System.out.println("The base of users is empty");
            return;
        }

        System.out.println("Enter a user ID");
        int userId = -1;
        while (true) {
            System.out.print("-> ");
            try {
                userId = Integer.parseInt(scanner.nextLine());
                System.out.printf("%s - %d\n", service.getUserName(userId), service.getUserBalance(userId));
                break;
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                break;
            } catch (Exception ignore) {
            }
            System.out.println("Wrong input. Repeat please");
        }
    }

    void performTransfer() {
        if (service.getNumberOfUsers() < 2) {
            System.out.println("The base have not enough count of users");
            return;
        }

        Integer sender = null;
        Integer reciver = null;
        Integer amount = null;

        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount (example: 2 5 50)");
        while (true) {
            System.out.print("-> ");
            try {
                String input = scanner.nextLine();

                Scanner sc = new Scanner(input);
                sender = sc.nextInt();
                reciver = sc.nextInt();
                amount = sc.nextInt();
                sc.close();

                if (service.performTransfer(sender, reciver, amount)) {
                    System.out.println("Transaction done");
                } else {
                    System.out.println("Transaction creating error");
                }

                break;
            } catch (IllegalTransactionException | UserNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception ignore) {
            }
            System.out.println("Wrong input. Repeat please");
        }
    }

    void printUserTransactions() {
        if (service.getNumberOfUsers() == 0) {
            System.out.println("The base of users is empty");
            return;
        }

        System.out.println("Enter a user ID");
        int userId = -1;
        while (true) {
            System.out.print("-> ");
            try {
                userId = Integer.parseInt(scanner.nextLine());
                Transaction[] transactions = service.getUserTransactions(userId);
                for (Transaction tr : transactions) {
                    String fromTo = (tr.getCategory() == Transaction.TransferCategory.CREDIT) ? "To" : "From";
                    System.out.printf("%-4s %-10s(id = %2d) %5d with id = %s\n",
                            fromTo, tr.getRecipient().getName(), tr.getRecipient().getId(), tr.getAmount(), tr.getId());
                }
                break;
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                break;
            } catch (Exception ignore) {
            }
            System.out.println("Wrong input. Repeat please");
        }
    }

    void removeTransfer() {
        if (service.getNumberOfUsers() == 0) {
            System.out.println("The base of users is empty");
            return;
        }

        System.out.println("Enter a user ID and a transfer ID (example: 2 19xxx43e-xx1x-49b4-88c0-bb5751a5d28)");

        Transaction tr = null;
        while (true) {
            System.out.print("-> ");
            try {
                String input = scanner.nextLine();

                Scanner sc = new Scanner(input);
                Integer userId = sc.nextInt();
                UUID transactionId = UUID.fromString(sc.next());
                sc.close();

                tr = service.removeTransaction(userId, transactionId);
                break;
            } catch (UserNotFoundException | TransactionNotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
                break;
            } catch (Exception ignore) {
            }
            System.out.println("Wrong input. Repeat please");
        }
        if (tr == null) {
            return;
        }

        String fromTo = (tr.getCategory() == Transaction.TransferCategory.CREDIT) ? "to" : "from";
        System.out.printf("Transfer %-4s %s(id = %d) %d removed\n",
                fromTo, tr.getRecipient().getName(), tr.getRecipient().getId(), tr.getAmount());
    }

    void checkTransferValidity() {
        if (service.getNumberOfUsers() == 0) {
            System.out.println("The base of users is empty");
            return;
        }

        Transaction[] badTr = service.validateTransactions();
        System.out.println("Check results:");

        if (badTr.length == 0) {
            System.out.println("All transactions is fine");
        } else {
            System.out.println("Unacknowledged transfers:");
        }

        for (Transaction t : badTr) {
            String fromTo = (t.getCategory() == Transaction.TransferCategory.CREDIT) ? "to" : "from";
            System.out.printf("%s(id = %d) has an unacknowledged transfer id = %s %s %s(id = %d) for %d\n",
                    t.getSender().getName(), t.getSender().getId(), t.getId(), fromTo,
                    t.getRecipient().getName(), t.getRecipient().getId(), t.getAmount());
        }
    }
}
