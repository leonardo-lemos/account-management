package uol.compass.accountmanagement.validation.constraint;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uol.compass.accountmanagement.model.enumeration.AccountTransactionType;
import uol.compass.accountmanagement.validation.ValidAccountTransactionType;

import java.util.Arrays;

public class ValidAccountTransactionTypeConstraint implements ConstraintValidator<ValidAccountTransactionType, AccountTransactionType> {

    private AccountTransactionType[] subset;

    @Override
    public void initialize(ValidAccountTransactionType constraint) {
        subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(AccountTransactionType accountType, ConstraintValidatorContext constraintValidatorContext) {
        return accountType == null || Arrays.asList(subset).contains(accountType);
    }
}
