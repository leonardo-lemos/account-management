package uol.compass.accountmanagement.validation;

import jakarta.validation.Constraint;
import uol.compass.accountmanagement.validation.constraint.ValidBirthdayConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBirthdayConstraint.class)
public @interface ValidBirthday {

    String message() default "Invalid birthday";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};

}
