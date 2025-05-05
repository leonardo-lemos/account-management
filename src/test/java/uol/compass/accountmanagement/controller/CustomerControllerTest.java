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
import uol.compass.accountmanagement.model.dto.CustomerRequestDTO;
import uol.compass.accountmanagement.model.dto.CustomerResponseDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControllerTest {

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    @Autowired
    private CustomerController customerController;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(new AccountManagementControllerAdvice())
                .build();
    }

    @Test
    public void shouldCreateAndRetrieveCustomerWithSuccess() throws Exception {
        // Given
        var request = CustomerRequestDTO.builder()
                .name("John Doe")
                .email("jhon@doe.com")
                .birthDate(LocalDate.of(1995, 3, 10))
                .build();

        // When
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Then
        mockMvc.perform(get("/api/v1/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponseDTO.class);
                    assertEquals(request.name(), response.name());
                    assertEquals(request.email(), response.email());
                    assertEquals(request.birthDate(), response.birthDate());
                });
    }

    @Test
    public void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        // When
        mockMvc.perform(get("/api/v1/customers/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestWhenCustomerIsInvalid() throws Exception {
        // Given
        var request = CustomerRequestDTO.builder()
                .name("")
                .email("invalid-email")
                .birthDate(LocalDate.now())
                .build();

        // When
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateCustomerWithSuccess() throws Exception {
        // Given
        var createRequest = CustomerRequestDTO.builder()
                .name("José Doe")
                .email("jose@doe.com")
                .birthDate(LocalDate.of(1988, 5, 15))
                .build();

        var updateRequest = CustomerRequestDTO.builder()
                .name("Jane Doe")
                .email("jane@doe.com")
                .birthDate(LocalDate.of(1991, 4, 19))
                .build();

        // When
        var resourceResponse = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(put(resourceResponse)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/api/v1/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponseDTO.class);
                    assertEquals(updateRequest.name(), response.name());
                    assertEquals(updateRequest.email(), response.email());
                    assertEquals(updateRequest.birthDate(), response.birthDate());
                });
    }


}