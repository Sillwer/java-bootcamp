package school21.spring.service.models;

public class User {
    private Long identifier;
    private String email;
    private String password;

    public User(Long identifier, String email, String password) {
        this.identifier = identifier;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User: {identifier=%d, email=%s, password=%s}", identifier, email, password);
    }

    @Override
    public int hashCode() {
        return new Long(identifier + email + password).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null
                    && this.getClass().equals(o.getClass())
                    && this.hashCode() == o.hashCode();
        }
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
