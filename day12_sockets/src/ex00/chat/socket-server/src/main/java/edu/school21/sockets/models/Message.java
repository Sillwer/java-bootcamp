package edu.school21.sockets.models;

import java.sql.Timestamp;

public class Message {
    private Long id;
    private String content;
    private Long chatRoomId;
    private Long authorId;
    private Timestamp timestamp;

    public Message(Long id, String content, Long chatRoomId, Long authorId, Timestamp timestamp) {
        this.id = id;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.authorId = authorId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        return (content + id + chatRoomId + authorId + timestamp).hashCode();
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
        return String.format("{id=%d, context=\"%s\", chatRoomId=%d, authorId=%d, timestamp=%s}",
                id, content, chatRoomId, authorId, timestamp);
    }
}
