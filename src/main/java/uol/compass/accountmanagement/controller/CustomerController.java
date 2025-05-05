package uol.compass.accountmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uol.compass.accountmanagement.model.dto.CustomerRequestDTO;
import uol.compass.accountmanagement.model.dto.CustomerResponseDTO;
import uol.compass.accountmanagement.service.CustomerService;
import uol.compass.accountmanagement.validation.group.CreateCustomerGroup;
import uol.compass.accountmanagement.validation.group.UpdateCustomerGroup;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    protected CustomerResponseDTO retrieveCustomer(@PathVariable Long id) {
        return customerService.retrieveCustomerById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> createCustomer(@Validated({CreateCustomerGroup.class}) @RequestBody CustomerRequestDTO request) {
        var response = customerService.createCustomer(request);

        return ResponseEntity.created(URI.create("/api/v1/customers/" + response.id())).build();
    }

    @PutMapping("/{id}")
    protected ResponseEntity<?> updateCustomer(@Validated({UpdateCustomerGroup.class}) @RequestBody CustomerRequestDTO request, @PathVariable Long id) {
        customerService.updateCustomer(id, request);
        return ResponseEntity.noContent().build();
    }

}
