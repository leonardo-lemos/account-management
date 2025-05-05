package uol.compass.accountmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uol.compass.accountmanagement.exception.AccountNotFoundException;
import uol.compass.accountmanagement.exception.CustomerNotFoundException;
import uol.compass.accountmanagement.exception.InsufficientFundsException;

@RestControllerAdvice
@Slf4j
public class AccountManagementControllerAdvice {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @SuppressWarnings("unused")
    public void handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.error(ex.getMessage(), ex);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @SuppressWarnings("unused")
    public void handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error(ex.getMessage(), ex);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SuppressWarnings("unused")
    public void handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error(ex.getMessage(), ex);
    }

}
