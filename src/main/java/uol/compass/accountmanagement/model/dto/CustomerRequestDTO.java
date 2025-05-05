package uol.compass.accountmanagement.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import uol.compass.accountmanagement.validation.ValidBirthday;
import uol.compass.accountmanagement.validation.group.CreateCustomerGroup;
import uol.compass.accountmanagement.validation.group.UpdateCustomerGroup;

import java.time.LocalDate;

@Builder
public record CustomerRequestDTO(
        @NotEmpty(groups = {CreateCustomerGroup.class, UpdateCustomerGroup.class})
        @Length(max = 30, groups = {CreateCustomerGroup.class, UpdateCustomerGroup.class})
        String name,
        @Email(groups = {CreateCustomerGroup.class, UpdateCustomerGroup.class})
        @Length(max = 50, groups = {CreateCustomerGroup.class, UpdateCustomerGroup.class})
        String email,
        @ValidBirthday(groups = {CreateCustomerGroup.class, UpdateCustomerGroup.class})
        LocalDate birthDate) {

}
