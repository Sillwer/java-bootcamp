package ex00;

public class User {
    private Integer id;
    private String name;
    private Integer balance;
    static private Integer usersCount = 0;

    private User() {
    }

    private User(String name, Integer balance) {
        this.name = name;
        this.balance = balance;
    }

    static User createUser(String name, Integer balance) {
        User usr = new User();
        boolean success = usr.setBalance(balance);
        if (success) {
            usr.setId(++usersCount);
            usr.setName(name);
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
}
