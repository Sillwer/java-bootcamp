package ex03;

public class User {
    private Integer id;
    private String name;
    private Integer balance;
    private TransactionsList transactionsList;

    private User() {
    }

    static User createUser(String name, Integer balance) {
        User usr = new User();
        boolean success = usr.setBalance(balance);
        if (success) {
            usr.setId(UserIdsGenerator.generateId());
            usr.setName(name);
            usr.setTransactionsList(new TransactionsLinkedList());
            return usr;
        } else {
            return null;
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean setBalance(Integer balance) {
        if (balance < 0) {
            return false;
        }
        this.balance = balance;
        return true;
    }

    public Integer getBalance() {
        return balance;
    }

    private void setTransactionsList(TransactionsList transactionsList) {
        this.transactionsList = transactionsList;
    }

    public TransactionsList getTransactionsList() {
        return transactionsList;
    }
}
