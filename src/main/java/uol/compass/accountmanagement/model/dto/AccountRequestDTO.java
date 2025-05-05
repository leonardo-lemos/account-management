package uol.compass.accountmanagement.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import uol.compass.accountmanagement.model.enumeration.AccountType;
import uol.compass.accountmanagement.validation.ValidAccountType;
import uol.compass.accountmanagement.validation.group.CreateAccountGroup;
import uol.compass.accountmanagement.validation.group.UpdateAccountGroup;

@Builder
public record AccountRequestDTO(
        @NotNull(groups = {UpdateAccountGroup.class})
        @Min(value = 1, groups = {UpdateAccountGroup.class})
        Long id,
        @NotNull(groups = {CreateAccountGroup.class, UpdateAccountGroup.class})
        @Min(value = 1, groups = {CreateAccountGroup.class, UpdateAccountGroup.class})
        Long customerId,
        @ValidAccountType(anyOf = {AccountType.CHECKING, AccountType.SAVINGS}, groups = {CreateAccountGroup.class, UpdateAccountGroup.class})
        AccountType type
) {
}
