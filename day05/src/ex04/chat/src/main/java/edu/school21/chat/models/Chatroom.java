package edu.school21.chat.models;

import java.util.HashMap;

public class Chatroom {
    private Long id;
    private String name;
    private User owner;
    private HashMap<Long, Chatroom> messages;

    public Chatroom(Long id, String name, User owner, HashMap<Long, Chatroom> messages) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    @Override
    public String toString() {
        String msgs = messages == null ? "null" : "(count: " + messages.size() + ")";
        String own = owner == null ? "null" : "(id: " + owner.getId() + ")";

        return String.format("{id=%d, name='%s', creator=%s, messages=%s}",
                id, name, own, msgs);
    }

    @Override
    public int hashCode() {
        return String.join("",
                id.toString(),
                name,
                owner == null ? "" : owner.getId().toString(),
                messages == null ? "" : Integer.toString(messages.hashCode())
        ).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else if (this == obj || this.hashCode() == obj.hashCode()) {
            return true;
        } else {
            return false;
        }
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HashMap<Long, Chatroom> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<Long, Chatroom> messages) {
        this.messages = messages;
    }
}