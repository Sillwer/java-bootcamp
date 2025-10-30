package edu.school21.sockets.models;

public class Chatroom {
    private Long id;
    private String name;
    private Long ownerId;

    public Chatroom(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int hashCode() {
        return (name + id + ownerId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        } else {
            return this.hashCode() == obj.hashCode();
        }
    }

    @Override
    public String toString() {
        return String.format("{id=%d, name=\"%s\", ownerId=%d}", id, name, ownerId);
    }
}
