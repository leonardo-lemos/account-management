package uol.compass.accountmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uol.compass.accountmanagement.model.enumeration.AccountType;
import uol.compass.accountmanagement.validation.constraint.ValidAccountTypeConstraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidAccountTypeConstraint.class)
public @interface ValidAccountType {

    AccountType[] anyOf();

    String message() default "Invalid account type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
