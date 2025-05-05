package uol.compass.accountmanagement.service;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uol.compass.accountmanagement.exception.AccountNotFoundException;
import uol.compass.accountmanagement.exception.CustomerNotFoundException;
import uol.compass.accountmanagement.exception.InsufficientFundsException;
import uol.compass.accountmanagement.model.Account;
import uol.compass.accountmanagement.model.AccountTransaction;
import uol.compass.accountmanagement.model.Customer;
import uol.compass.accountmanagement.model.dto.*;
import uol.compass.accountmanagement.model.enumeration.AccountTransactionType;
import uol.compass.accountmanagement.repository.AccountRepository;
import uol.compass.accountmanagement.repository.AccountTransactionRepository;
import uol.compass.accountmanagement.repository.CustomerRepository;
import uol.compass.accountmanagement.validation.group.CreateAccountGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountManagementService {

    private final AccountRepository accountRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    private final CustomerRepository customerRepository;

    public AccountResponseDTO createAccount(@Validated({CreateAccountGroup.class}) AccountRequestDTO request) {
        var customer = customerRepository
                .findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.customerId()));

        var entity = buildEntityFromRequestAndCustomer(request, customer);

        accountRepository.save(entity);

        return buildResponseFromEntityAndCustomer(entity, customer);
    }

    public AccountResponseDTO retrieveAccountById(Long id) {
        var account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return buildResponseFromEntityAndCustomer(account, account.getCustomer());
    }

    public void deposit(@Validated @Min(1) Long accountId, @Validated TransactionRequestDTO request) {
        var account = persistTransaction(accountId, request, AccountTransactionType.DEPOSIT);

        account.setBalance(account.getBalance().add(request.amount()));
        accountRepository.save(account);
    }

    public void withdraw(@Validated @Min(1) Long accountId, @Validated TransactionRequestDTO request) {
        var account = persistTransaction(accountId, request, AccountTransactionType.WITHDRAW);

        account.setBalance(account.getBalance().subtract(request.amount()));
        accountRepository.save(account);
    }

    // Outra alternativa a essa implementação seria criar uma view no banco de dados para simplificar a busca
    public TransactionReportDTO generateTransactionReport(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        var account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        var transactions = accountTransactionRepository
                .findAllByAccountAndPeriod(accountId, startDate, endDate);

        return TransactionReportDTO.builder()
                .customer(CustomerResponseDTO.builder()
                        .id(account.getCustomer().getId())
                        .email(account.getCustomer().getEmail())
                        .birthDate(account.getCustomer().getBirthDate())
                        .name(account.getCustomer().getName())
                        .accounts(List.of(AccountResponseDTO.builder()
                                .id(accountId)
                                .balance(account.getBalance())
                                .build())
                        )
                        .build()
                )
                .periodStart(startDate)
                .periodEnd(endDate)
                .transactions(transactions.stream()
                        .map(x -> new TransactionResponseDTO(
                                x.getId(),
                                x.getAccount().getId(),
                                x.getType(),
                                x.getAmount(),
                                x.getTimestamp()
                        )).toList()
                )
                .build();
    }

    private Account persistTransaction(Long accountId, TransactionRequestDTO request, AccountTransactionType type) {
        var account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (type == AccountTransactionType.WITHDRAW && account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException();
        }

        var transaction = AccountTransaction.builder()
                .account(account)
                .amount(request.amount())
                .type(type)
                .build();

        accountTransactionRepository.save(transaction);
        return account;
    }

    private AccountResponseDTO buildResponseFromEntityAndCustomer(Account entity, Customer customer) {
        return AccountResponseDTO.builder()
                .id(entity.getId())
                .balance(entity.getBalance())
                .type(entity.getType())
                .customerId(customer.getId())
                .build();
    }

    private Account buildEntityFromRequestAndCustomer(AccountRequestDTO request, Customer customer) {
        return Account.builder()
                .customer(customer)
                .balance(BigDecimal.ZERO)
                .type(request.type())
                .build();
    }
}
