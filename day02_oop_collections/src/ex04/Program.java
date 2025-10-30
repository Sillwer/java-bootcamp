package ex04;

import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        boolean statusOk = true;
        TransactionsService service = new TransactionsService();

        statusOk = service.addUser("Vik", 200);
        if (!statusOk) {
            System.out.println("При добавлении пользователя произошла ошибка");
            System.exit(-1);
        }

        statusOk = service.addUser("Nikita", 1210);
        if (!statusOk) {
            System.out.println("При добавлении пользователя произошла ошибка");
            System.exit(-1);
        }

        service.printUsersList();
        System.out.println();

        try {
            service.performTransfer(1, 2, 30);
            service.performTransfer(1, 2, 20);
            System.out.println("Transaction done");
        } catch (IllegalTransactionException ite) {
            System.out.println(ite.getMessage());
        } catch (Error e) {
            System.out.println("Не предвиденная ошибка при создании транзакции");
        }

        System.out.println("\nTransactions of user with id = 1\n" + Transaction.getHeader());
        for (Transaction tr : service.getUserTransactions(1)) {
            System.out.println(tr);
        }
        Integer usrBalance = service.getUserBalance(1);
        if (usrBalance != null) {
            System.out.printf("\nUser with id %d have balance = %d\n", 1, usrBalance);
        }

        System.out.println();
        UUID uuid = service.getUserTransactions(1)[0].getId();
        if (service.removeTransaction(1, uuid)) {
            System.out.printf("Transaction %s was deleted about user with id %d\n", uuid, 1);
        } else {
            System.out.printf("Transaction %s NOT deleted about user with id %d\n", uuid, 1);
        }

        System.out.println("\nTransactions of user with id = 1\n" + Transaction.getHeader());
        for (Transaction tr : service.getUserTransactions(1)) {
            System.out.println(tr);
        }

        System.out.println();
        Transaction[] badTransactions = service.validateTransactions();
        if (badTransactions.length == 0) {
            System.out.println("Have not bad transactions");
        } else {
            System.out.println("Bad transactions:");
            System.out.println(Transaction.getHeader());
            for (Transaction tr : badTransactions) {
                System.out.println(tr);
            }
        }
    }
}
