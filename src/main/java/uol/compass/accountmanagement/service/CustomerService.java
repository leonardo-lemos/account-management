package uol.compass.accountmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uol.compass.accountmanagement.exception.CustomerNotFoundException;
import uol.compass.accountmanagement.model.Account;
import uol.compass.accountmanagement.model.Customer;
import uol.compass.accountmanagement.model.dto.AccountResponseDTO;
import uol.compass.accountmanagement.model.dto.CustomerRequestDTO;
import uol.compass.accountmanagement.model.dto.CustomerResponseDTO;
import uol.compass.accountmanagement.repository.CustomerRepository;
import uol.compass.accountmanagement.validation.group.CreateCustomerGroup;
import uol.compass.accountmanagement.validation.group.UpdateCustomerGroup;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponseDTO retrieveCustomerById(Long id) {
        var entity = customerRepository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return buildResponseFromEntity(entity, entity.getAccounts());
    }

    public CustomerResponseDTO createCustomer(@Validated(CreateCustomerGroup.class) CustomerRequestDTO request) {
        var entity = buildEntityFromRequest(request);

        customerRepository.save(entity);

        return buildResponseFromEntity(entity, List.of());
    }

    public void updateCustomer(Long id, @Validated({CreateCustomerGroup.class, UpdateCustomerGroup.class}) CustomerRequestDTO request) {
        var entity = customerRepository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setBirthDate(request.birthDate());

        customerRepository.save(entity);
    }

    private CustomerResponseDTO buildResponseFromEntity(Customer savedCustomer, List<Account> customerAccounts) {
        return CustomerResponseDTO.builder()
                .id(savedCustomer.getId())
                .name(savedCustomer.getName())
                .email(savedCustomer.getEmail())
                .birthDate(savedCustomer.getBirthDate())
                .accounts(customerAccounts
                        .stream()
                        .map(x -> new AccountResponseDTO(
                                x.getId(),
                                savedCustomer.getId(),
                                x.getBalance(),
                                x.getType()
                        ))
                        .toList()
                )
                .build();
    }

    private Customer buildEntityFromRequest(CustomerRequestDTO request) {
        return Customer.builder()
                .birthDate(request.birthDate())
                .name(request.name())
                .email(request.email())
                .build();
    }
}
