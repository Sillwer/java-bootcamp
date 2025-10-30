package ex04;

public class IllegalTransactionException extends ArithmeticException {
    IllegalTransactionException(String msg) {
        super(msg);
    }
}
