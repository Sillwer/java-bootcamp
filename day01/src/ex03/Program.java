package ex03;

import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        UsersList users = new UsersArrayList();

        users.addUser(User.createUser("Vik", 200));
        users.addUser(User.createUser("Nikita", 1210));
        for (User usr : users) {
            if (usr == null) {
                System.out.println("Ошибка при создании пользователей");
                System.exit(-1);
            }
        }

        User u1 = users.getUserByIndex(0), u2 = users.getUserByIndex(1);

        Transaction[] tr = Transaction.createTransaction(u1, u2, 50);
        if (tr == null) {
            System.out.println("Ошибка при создании транзакции");
            System.exit(-1);
        }
        u1.getTransactionsList().addTransaction(tr[0]);
        u2.getTransactionsList().addTransaction(tr[1]);

        Transaction[] tr2 = Transaction.createTransaction(u1, u2, 40);
        if (tr2 == null) {
            System.out.println("Ошибка при создании транзакции");
            System.exit(-1);
        }
        u1.getTransactionsList().addTransaction(tr2[0]);
        u2.getTransactionsList().addTransaction(tr2[1]);

        System.out.println("\nUsers:");
        System.out.printf("%5s %10s %10s\n", "ID", "Name", "Balance");
        for (User usr : users) {
            System.out.printf("%5d %10s %10d\n", usr.getId(), usr.getName(), usr.getBalance());
            System.out.printf("\t%-36s %13s %13s %10s %10s\n", "Transactions: ID", "Sender ID", "Recipient ID", "Type", "Amount");
            for (Transaction t : usr.getTransactionsList().toArray()) {
                System.out.printf("\t%36s %13d %13d %10s %10s\n",
                        t.getId(), t.getSender().getId(), t.getRecipient().getId(), t.getCategory(), t.getAmount());
            }
            System.out.println();
        }

        UUID uuid = u1.getTransactionsList().toArray()[1].getId();
        System.out.printf("Удаление узла с uuid = %s у пользователя с id =%d\n", uuid, u1.getId());
        try {
            u1.getTransactionsList().removeTransaction(uuid);
            System.out.println("Транзакция удалена");
        } catch (TransactionNotFoundException nfe) {
            System.out.println(nfe.getMessage());
        } catch (Exception e) {
            System.out.printf("Неожиданная ошибка: %s\n", e.getMessage());
        }

        System.out.println("\nUsers:");
        System.out.printf("%5s %10s %10s\n", "ID", "Name", "Balance");
        for (User usr : users) {
            System.out.printf("%5d %10s %10d\n", usr.getId(), usr.getName(), usr.getBalance());
            System.out.printf("\t%-36s %13s %13s %10s %10s\n", "Transactions: ID", "Sender ID", "Recipient ID", "Type", "Amount");
            for (Transaction t : usr.getTransactionsList().toArray()) {
                System.out.printf("\t%36s %13d %13d %10s %10s\n",
                        t.getId(), t.getSender().getId(), t.getRecipient().getId(), t.getCategory(), t.getAmount());
            }
            System.out.println();
        }
    }
}
