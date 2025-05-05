package uol.compass.accountmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uol.compass.accountmanagement.model.dto.AccountRequestDTO;
import uol.compass.accountmanagement.service.AccountManagementService;
import uol.compass.accountmanagement.validation.group.CreateAccountGroup;

import java.net.URI;

/*
 * Listening to:
 * Track: Adolf
 * Artist: PIERROT
 * Album: Celluloid
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountManagementService accountManagementService;

    @GetMapping("/{id}")
    protected ResponseEntity<?> retrieveAccount(@PathVariable Long id) {
        var response = accountManagementService.retrieveAccountById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    protected ResponseEntity<?> createAccount(@Validated({CreateAccountGroup.class}) @RequestBody AccountRequestDTO request) {
        var response = accountManagementService.createAccount(request);

        return ResponseEntity.created(URI.create("/api/v1/accounts/" + response.id())).build();
    }
}
