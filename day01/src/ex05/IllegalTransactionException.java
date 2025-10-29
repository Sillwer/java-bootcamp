package ex05;

public class IllegalTransactionException extends ArithmeticException {
    IllegalTransactionException(String msg) {
        super(msg);
    }
}
