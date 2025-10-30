package edu.school.game.exceptions;

public class UserLostException extends RuntimeException {
    public UserLostException(String msg) {
        super(msg);
    }
}
