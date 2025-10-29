package ex02;

public class Program {
    public static void main(String[] args) {
        UsersList users = new UsersArrayList();

        users.addUser(User.createUser("Vik", 200));
        users.addUser(User.createUser("Nikita", 1210));
        users.addUser(User.createUser("Name1", 32));
        users.addUser(User.createUser("Name1", 123));
        users.addUser(User.createUser("Name2", 4324));
        users.addUser(User.createUser("Name3", 13));
        users.addUser(User.createUser("Name4", 432));
        users.addUser(User.createUser("Name5", 543));
        users.addUser(User.createUser("Name6", 234));
        users.addUser(User.createUser("Name7", 6546456));
        users.addUser(User.createUser("Name8", 543));
        users.addUser(User.createUser("Name9", 4));

        for (User usr : users) {
            if (usr == null) {
                System.out.println("Ошибка при создании пользователей");
                System.exit(-1);
            }
        }

        Transaction[] tr = Transaction.createTransaction(users.getUserByIndex(0), users.getUserByIndex(1), 50);
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


        System.out.printf("\ngetNumberOfUsers(): %d\n", users.getNumberOfUsers());

        System.out.println("\ngetUserById(3):");
        User u = null;
        try {
            u = users.getUserById(2);
            System.out.printf("%5d %10s %10d\n", u.getId(), u.getName(), u.getBalance());
        } catch (UserNotFoundException nfe) {
            System.out.println(nfe.getMessage());
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка при поиске пользователя по id: " + e.getMessage());
        }

        System.out.println("\ngetUserById(156):");
        try {
            u = users.getUserById(156);
            System.out.printf("%5d %10s %10d\n", u.getId(), u.getName(), u.getBalance());
        } catch (UserNotFoundException nfe) {
            System.out.println(nfe.getMessage());
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка при поиске пользователя по id: " + e.getMessage());
        }
    }
}
