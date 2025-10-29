package ex03;

public class UserNotFoundException extends IndexOutOfBoundsException {
    UserNotFoundException(String msg) {
        super(msg);
    }
}
