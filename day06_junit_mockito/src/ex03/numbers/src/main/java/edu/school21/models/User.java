package edu.school21.models;

public class User {
    private Long id;
    private String login;
    private String password;
    private Boolean authorized;

    public User(Long id, String login, String password, Boolean authorized) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authorized = authorized;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }
}
