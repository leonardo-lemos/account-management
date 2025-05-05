package uol.compass.accountmanagement.validation.constraint;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uol.compass.accountmanagement.model.enumeration.AccountType;
import uol.compass.accountmanagement.validation.ValidAccountType;

import java.util.Arrays;

public class ValidAccountTypeConstraint implements ConstraintValidator<ValidAccountType, AccountType> {

    private AccountType[] subset;

    @Override
    public void initialize(ValidAccountType constraint) {
        subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(AccountType accountType, ConstraintValidatorContext constraintValidatorContext) {
        return accountType == null || Arrays.asList(subset).contains(accountType);
    }
}
