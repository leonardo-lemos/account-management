package uol.compass.accountmanagement.model.dto;

import lombok.Builder;
import uol.compass.accountmanagement.model.enumeration.AccountType;

import java.math.BigDecimal;

@Builder
public record AccountResponseDTO(
        Long id,
        Long customerId,
        BigDecimal balance,
        AccountType type
) {
}
