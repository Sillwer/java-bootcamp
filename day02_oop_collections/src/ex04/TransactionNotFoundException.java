package ex04;

public class TransactionNotFoundException extends RuntimeException {
    TransactionNotFoundException(String msg) {
        super(msg);
    }
}
