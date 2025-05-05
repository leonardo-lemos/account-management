package uol.compass.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uol.compass.accountmanagement.model.dto.AccountRequestDTO;
import uol.compass.accountmanagement.model.dto.AccountResponseDTO;
import uol.compass.accountmanagement.model.dto.CustomerRequestDTO;
import uol.compass.accountmanagement.model.enumeration.AccountType;
import uol.compass.accountmanagement.service.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(new AccountManagementControllerAdvice()).build();
    }

    @Test
    void accountControllerShouldCreateAndRetrieveAccountWithSuccess() throws Exception {
        var request = AccountRequestDTO.builder()
                .type(AccountType.CHECKING)
                .customerId(1L)
                .build();
        var response = AccountResponseDTO.builder()
                .id(1L)
                .customerId(1L)
                .balance(BigDecimal.ZERO)
                .type(AccountType.CHECKING)
                .build();

        customerService.createCustomer(CustomerRequestDTO.builder()
                .name("John Doe")
                .email("jhon@doe.com")
                .birthDate(LocalDate.of(1995, 3, 10))
                .build());

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/accounts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var accountResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AccountResponseDTO.class);
                    assert accountResponse.id().equals(response.id());
                    assert accountResponse.customerId().equals(response.customerId());
                    assert accountResponse.balance().compareTo(response.balance()) == 0;
                    assert accountResponse.type().equals(response.type());
                });
    }

    @Test
    void createAccountReturns400WhenRequestBodyIsInvalid() throws Exception {
        var invalidRequest = AccountRequestDTO.builder().build();

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retrieveAccountReturns404WhenAccountDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}