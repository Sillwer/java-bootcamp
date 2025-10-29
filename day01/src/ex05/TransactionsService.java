package ex05;

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

    Integer getUserBalance(Integer usrId) throws UserNotFoundException {
        return usersList.getUserById(usrId).getBalance();
    }

    boolean performTransfer(Integer senderId, Integer recipientId, Integer amount) throws IllegalTransactionException, UserNotFoundException {
        User sender;
        User recipient;
        try {
            sender = usersList.getUserById(senderId);
            recipient = usersList.getUserById(recipientId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Unknown sender/recipient of transaction");
        }

        // отклоняем транзакции самому себе
        if (sender == recipient) {
            throw new IllegalTransactionException("Cannot make a transfer to yourself");
        }

        // проверить возможность транзакции
        if (amount > 0 && sender.getBalance() < amount) {
            throw new IllegalTransactionException("Sender have not nought money");
        }

        // создать транзакцию (2 с одним ИД)
        Transaction[] tr = Transaction.createTransaction(sender, recipient, amount);
        if (tr == null) {
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

    Transaction[] getUserTransactions(Integer usrId) throws UserNotFoundException {
        return usersList.getUserById(usrId).getTransactionsList().toArray();
    }

    Transaction removeTransaction(Integer usrId, UUID transactionID) throws UserNotFoundException, TransactionNotFoundException {
        User usr = usersList.getUserById(usrId);
        return usr.getTransactionsList().removeTransaction(transactionID);
    }

    Transaction[] validateTransactions() {
        TransactionsList badTransactionsList = new TransactionsLinkedList();

        // пройти по всем пользакам
        for (User usr : usersList) {
            for (Transaction tr : usr.getTransactionsList().toArray()) {
                UUID transactionID = tr.getId();

                Transaction pairTr = tr.getRecipient().getTransactionsList().getTransactionByID(transactionID);
                if (pairTr == null || !pairTr.getId().equals(transactionID)) {
                    badTransactionsList.addTransaction(tr);
                }
            }
        }

        return badTransactionsList.toArray();
    }

    void printUsersList() {
        System.out.println("\nUsers:\n" + User.getHeader());
        for (User usr : usersList) {
            System.out.println(usr);
        }
    }

    int getNumberOfUsers() {
        return usersList.getNumberOfUsers();
    }

    String getUserName(Integer userId) throws UserNotFoundException {
        return usersList.getUserById(userId).getName();
    }
}
