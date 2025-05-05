package uol.compass.accountmanagement.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TransactionReportDTO(
        CustomerResponseDTO customer,
        LocalDateTime periodStart,
        LocalDateTime periodEnd,
        List<TransactionResponseDTO> transactions
) {
}
