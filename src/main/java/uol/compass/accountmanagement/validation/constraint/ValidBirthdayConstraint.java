package uol.compass.accountmanagement.validation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uol.compass.accountmanagement.validation.ValidBirthday;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class ValidBirthdayConstraint implements ConstraintValidator<ValidBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.getYear() < ZonedDateTime.now().getYear() && date.getYear() > 1900;
    }
}
