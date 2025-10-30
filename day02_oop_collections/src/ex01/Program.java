package ex01;

public class Program {
    public static void main(String[] args) {
        User[] users = new User[2];
        users[0] = User.createUser("Vik", 200);
        users[1] = User.createUser("Nikita", 1210);
        if (users[0] == null || users[1] == null) {
            System.out.println("Ошибка при создании пользователей");
            System.exit(-1);
        }

        Transaction tr[] = Transaction.createTransaction(users[0], users[1], 50);
        if (tr == null) {
            System.out.println("Ошибка при создании транзакции");
            System.exit(-1);
        }

        System.out.println("Users:");
        System.out.printf("%5s %10s %10s\n", "ID", "Name", "Balance");
        for (User usr : users) {
            System.out.printf("%5d %10s %10d\n", usr.getId(), usr.getName(), usr.getBalance());
        }

        System.out.println("Transactions:");
        System.out.printf("%36s %13s %13s %10s %10s\n", "ID", "Sender ID", "Recipient ID", "Type", "Amount");
        for (Transaction t : tr) {
            System.out.printf("%36s %13d %13d %10s %10s\n",
                    t.getId(), t.getSender().getId(), t.getRecipient().getId(), t.getCategory(), t.getAmount());
        }
    }
}
