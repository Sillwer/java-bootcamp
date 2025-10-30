package ex05;

public class UserNotFoundException extends IndexOutOfBoundsException {
    UserNotFoundException(String msg) {
        super(msg);
    }
}
