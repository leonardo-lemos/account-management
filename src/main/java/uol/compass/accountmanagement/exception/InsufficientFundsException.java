package uol.compass.accountmanagement.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Insufficient funds for withdrawal.");
    }
}
