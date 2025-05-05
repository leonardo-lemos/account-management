package uol.compass.accountmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uol.compass.accountmanagement.model.enumeration.AccountTransactionType;
import uol.compass.accountmanagement.validation.constraint.ValidAccountTransactionTypeConstraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidAccountTransactionTypeConstraint.class)
public @interface ValidAccountTransactionType {

    AccountTransactionType[] anyOf();

    String message() default "Invalid transaction type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
