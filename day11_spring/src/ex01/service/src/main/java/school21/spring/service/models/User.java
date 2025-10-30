package school21.spring.service.models;

public class User {
    private Long identifier;
    private String email;

    public User(Long identifier, String email) {
        this.identifier = identifier;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("User: {identifier=%d, email=%s}", identifier, email);
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
