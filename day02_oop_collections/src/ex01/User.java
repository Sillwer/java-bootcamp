package ex01;

public class User {
    private Integer id;
    private String name;
    private Integer balance;

    private User() {
    }

    static User createUser(String name, Integer balance) {
        User usr = new User();
        usr.name = name;
        boolean success = usr.setBalance(balance);
        if (success) {
            usr.setId(UserIdsGenerator.generateId());
            usr.setName(name);
            return usr;
        } else {
            return null;
        }
    }

    private void setId(Integer id) {
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
