package uol.compass.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uol.compass.accountmanagement.model.dto.*;
import uol.compass.accountmanagement.model.enumeration.AccountTransactionType;
import uol.compass.accountmanagement.model.enumeration.AccountType;
import uol.compass.accountmanagement.service.AccountManagementService;
import uol.compass.accountmanagement.service.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private AccountManagementService accountManagementService;
    @Autowired
    private CustomerService customerService;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setControllerAdvice(new AccountManagementControllerAdvice())
                .build();
    }

    @Test
    public void shouldDepositWithSuccess() throws Exception {
        var depositRequest = TransactionRequestDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .build();

        var accountResponse = setupCustomerAndAccount();

        var response = TransactionResponseDTO.builder()
                .id(1L)
                .accountId(accountResponse.id())
                .amount(BigDecimal.valueOf(100))
                .build();

        mockMvc.perform(post("/api/v1/transactions/{accountId}/deposit", accountResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/transactions/{accountId}/report",
                        accountResponse.id())
                        .param("startDate", LocalDateTime.now().minusDays(10L).toString())
                        .param("endDate", LocalDateTime.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var responseBody = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionReportDTO.class);

                    assertEquals(accountResponse.id(), responseBody.customer().accounts().getFirst().id());
                    assertEquals(1, responseBody.transactions().size());
                    assertEquals(response.id(), responseBody.transactions().getFirst().id());
                    assertEquals(response.accountId(), responseBody.transactions().getFirst().accountId());
                    assertEquals(0, response.amount().compareTo(responseBody.transactions().getFirst().amount()));
                    assertEquals(AccountTransactionType.DEPOSIT, responseBody.transactions().getFirst().type());
                });
    }

    @Test
    public void shouldWithdrawWithSuccess() throws Exception {
        var depositRequest = TransactionRequestDTO.builder()
                .amount(BigDecimal.valueOf(100L))
                .build();

        var withdrawRequest = TransactionRequestDTO.builder()
                .amount(BigDecimal.TEN)
                .build();

        var accountResponse = setupCustomerAndAccount();

        mockMvc.perform(post("/api/v1/transactions/{accountId}/deposit", accountResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/transactions/{accountId}/withdraw", accountResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/transactions/{accountId}/report",
                        accountResponse.id())
                        .param("startDate", LocalDateTime.now().minusDays(10L).toString())
                        .param("endDate", LocalDateTime.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var responseBody = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionReportDTO.class);

                    assertEquals(2, responseBody.transactions().size());
                    assertEquals(2L, responseBody.transactions().getLast().id());
                    assertEquals(accountResponse.id(), responseBody.transactions().getLast().accountId());
                    assertEquals(0, BigDecimal.TEN.compareTo(responseBody.transactions().getLast().amount()));
                    assertEquals(AccountTransactionType.WITHDRAW, responseBody.transactions().getLast().type());
                    assertEquals(0, BigDecimal.valueOf(90L).compareTo(responseBody.customer().accounts().getFirst().balance()));
                });
    }

    @Test
    public void shouldReturn400WhenWithdrawWithNoFunds() throws Exception {
        var request = TransactionRequestDTO.builder()
                .amount(BigDecimal.valueOf(100, 2))
                .build();

        var accountResponse = setupCustomerAndAccount();

        mockMvc.perform(post("/api/v1/transactions/{accountId}/withdraw", accountResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private AccountResponseDTO setupCustomerAndAccount() throws Exception {
        var customerRequest = CustomerRequestDTO.builder()
                .name("John Doe")
                .email("john@doe.com")
                .birthDate(LocalDate.of(1995, 3, 10))
                .build();


        var customerResponse = customerService.createCustomer(customerRequest);
        var accountRequest = AccountRequestDTO.builder()
                .type(AccountType.CHECKING)
                .customerId(customerResponse.id())
                .build();

        return accountManagementService.createAccount(accountRequest);
    }

}