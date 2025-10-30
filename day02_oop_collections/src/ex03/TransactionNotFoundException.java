package ex03;

public class TransactionNotFoundException extends RuntimeException {
    TransactionNotFoundException(String msg) {
        super(msg);
    }
}
