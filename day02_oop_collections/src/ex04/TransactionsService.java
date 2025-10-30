package ex04;

import java.util.UUID;

public class TransactionsService {
    private final UsersList usersList = new UsersArrayList();

    boolean addUser(String name, Integer balance) {
        User usr = User.createUser(name, balance);
        if (usr == null) {
            return false;
        }
        usersList.addUser(usr);
        return true;
    }

    Integer getUserBalance(Integer usrId) {
        try {
            return usersList.getUserById(usrId).getBalance();
        } catch (UserNotFoundException nfe) {
            System.out.println(nfe.getMessage());
        } catch (Exception e) {
            System.out.println("Not foreseen a mistake when trying to find out the user balance");
        }
        return null;
    }

    boolean performTransfer(Integer senderId, Integer recipientId, Integer amount) throws IllegalTransactionException {
        User sender = null;
        User recipient = null;
        try {
            sender = usersList.getUserById(senderId);
            recipient = usersList.getUserById(recipientId);
        } catch (UserNotFoundException e) {
            System.out.println("Unknown sender/recipient of transaction");
            return false;
        }

        // проверить возможность транзакции
        if (amount > 0 && sender.getBalance() < amount) {
            throw new IllegalTransactionException("Sender have not nought money");
        }

        // создать транзакцию (2 с одним ИД)
        Transaction[] tr = Transaction.createTransaction(sender, recipient, amount);
        if (tr == null) {
            System.out.println("Transaction creating error");
            return false;
        }

        // прописать соответствующие транзакции в списки пользаков
        sender.getTransactionsList().addTransaction(tr[0]);
        recipient.getTransactionsList().addTransaction(tr[1]);

        // списать бабки со счета пользака
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        return true;
    }

    Transaction[] getUserTransactions(Integer usrId) {
        try {
            return usersList.getUserById(usrId).getTransactionsList().toArray();
        } catch (UserNotFoundException nfe) {
            System.out.println(nfe.getMessage());
        }
        return null;
    }

    boolean removeTransaction(Integer usrId, UUID transactionID) {
        try {
            User usr = usersList.getUserById(usrId);
            usr.getTransactionsList().removeTransaction(transactionID);
        } catch (UserNotFoundException | TransactionNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    Transaction[] validateTransactions() {
        TransactionsList badTransactions = new TransactionsLinkedList();

        // пройти по всем пользакам
        for (User usr : usersList) {
            for (Transaction tr : usr.getTransactionsList().toArray()) {
                UUID transactionID = tr.getId();

                Transaction pairTr = null;
                if (tr.getCategory() == Transaction.TransferCategory.CREDIT) {
                    pairTr = tr.getRecipient().getTransactionsList().getTransactionByID(transactionID);
                } else {
                    pairTr = tr.getSender().getTransactionsList().getTransactionByID(transactionID);
                }

                if (pairTr == null || !pairTr.getId().equals(transactionID)) {
                    badTransactions.addTransaction(tr);
                }
            }
        }

        return badTransactions.toArray();
    }

    void printUsersList() {
        System.out.println("\nUsers:\n" + User.getHeader());
        for (User usr : usersList) {
            System.out.println(usr);
        }
    }
}
