package edu.school21.chat.models;

import java.util.HashMap;

public class User {
    private Long id;
    private String login;
    private String password;
    private HashMap<Long, Chatroom> socializesChatRooms;  // List of chat rooms where a user socializes
    private HashMap<Long, Chatroom> ownChatRooms;  // List of created rooms

    public User(Long id, String login, String password, HashMap<Long, Chatroom> socializesChatRooms, HashMap<Long, Chatroom> ownChatRooms) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.socializesChatRooms = socializesChatRooms;
        this.ownChatRooms = ownChatRooms;
    }

    @Override
    public String toString() {
        String ownRooms = ownChatRooms == null ? "null" : "(count: " + ownChatRooms.size() + ")";
        String rooms = socializesChatRooms == null ? "null" : "(count: " + socializesChatRooms.size() + ")";

        return String.format("{id=%d, login='%s', password='%s', createdRooms=%s, rooms=%s}",
                id, login, password, ownRooms, rooms);
    }

    @Override
    public int hashCode() {
        return String.join("",
                id.toString(),
                login,
                password,
                socializesChatRooms == null ? "" : Integer.toString(socializesChatRooms.hashCode()),
                ownChatRooms == null ? "" : Integer.toString(ownChatRooms.hashCode())
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<Long, Chatroom> getOwnChatRooms() {
        return ownChatRooms;
    }

    public void setOwnChatRooms(HashMap<Long, Chatroom> ownChatRooms) {
        this.ownChatRooms = ownChatRooms;
    }

    public HashMap<Long, Chatroom> getSocializesChatRooms() {
        return socializesChatRooms;
    }

    public void setSocializesChatRooms(HashMap<Long, Chatroom> socializesChatRooms) {
        this.socializesChatRooms = socializesChatRooms;
    }
}
