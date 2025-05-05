package uol.compass.accountmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.accountmanagement.model.dto.TransactionReportDTO;
import uol.compass.accountmanagement.model.dto.TransactionRequestDTO;
import uol.compass.accountmanagement.service.AccountManagementService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final AccountManagementService accountManagementService;

    @GetMapping(value = "/{accountId}/report", produces = MediaType.APPLICATION_JSON_VALUE)
    protected TransactionReportDTO getTransactionReport(@PathVariable Long accountId,
                                                        @RequestParam LocalDateTime startDate,
                                                        @RequestParam LocalDateTime endDate) {
        return accountManagementService.generateTransactionReport(accountId, startDate, endDate);
    }

    @PostMapping(value = "/{accountId}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> deposit(@PathVariable Long accountId,
                                        @RequestBody TransactionRequestDTO request) {
        accountManagementService.deposit(accountId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{accountId}/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> withdraw(@PathVariable Long accountId,
                                         @RequestBody TransactionRequestDTO request) {
        accountManagementService.withdraw(accountId, request);
        return ResponseEntity.noContent().build();
    }

}
