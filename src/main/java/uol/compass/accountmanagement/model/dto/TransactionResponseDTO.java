package uol.compass.accountmanagement.model.dto;

import lombok.Builder;
import uol.compass.accountmanagement.model.enumeration.AccountTransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponseDTO(
        Long id,
        Long accountId,
        AccountTransactionType type,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
