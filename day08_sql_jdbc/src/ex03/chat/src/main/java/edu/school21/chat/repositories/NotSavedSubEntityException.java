package edu.school21.chat.repositories;

public class NotSavedSubEntityException extends RuntimeException {
    NotSavedSubEntityException(String msg) {
        super(msg);
    }
}
