package uol.compass.accountmanagement.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account with ID " + id + " not found");
    }

}
