package uol.compass.accountmanagement.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionRequestDTO(
        @DecimalMin(value = "0.01", inclusive = false)
        @Digits(integer = 6, fraction = 2)
        BigDecimal amount
) {
}
