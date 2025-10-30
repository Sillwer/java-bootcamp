package edu.school21.chat.models;

import java.sql.Timestamp;

public class Message {
    private Long id;
    private User author;
    private Chatroom chatRoom;
    private String text;
    private Timestamp dateTime;

    public Message(Long id, User author, Chatroom chatRoom, String text, Timestamp dateTime) {
        this.id = id;
        this.author = author;
        this.chatRoom = chatRoom;
        this.text = text;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return String.format("Message : {\n" +
                        "\tid=%d,\n" +
                        "\tauthor=%s,\n" +
                        "\troom=%s,\n" +
                        "\ttext='%s',\n" +
                        "\tdateTime=%s\n" +
                        "}",
                id, author, chatRoom, text, dateTime);
    }

    @Override
    public int hashCode() {
        return String.join("",
                id.toString(),
                author == null ? "" : Integer.toString(author.hashCode()),
                chatRoom == null ? "" : Integer.toString(chatRoom.hashCode()),
                text,
                dateTime == null ? "" : dateTime.toString()
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Chatroom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(Chatroom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }
}
