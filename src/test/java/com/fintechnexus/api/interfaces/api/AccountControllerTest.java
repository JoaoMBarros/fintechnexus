package com.fintechnexus.api.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.application.dto.AccountCreationResponseDTO;
import com.fintechnexus.api.application.dto.ErrorResponseDTO;
import com.fintechnexus.api.application.service.AccountService;
import com.fintechnexus.api.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @ParameterizedTest
    @ArgumentsSource(AccountControllerArgumentProvider.class)
    void test_account_creation_parameters(AccountCreationDTO dto, HttpStatus expectedStatus, ErrorResponseDTO expectedError) throws Exception {
        if (expectedError == null) {
            when(accountService.createAccount(any()))
                    .thenReturn(new AccountCreationResponseDTO(
                            UUID.randomUUID(),
                            dto.documentNumber(),
                            dto.holderName(),
                            dto.accountType(),
                            Account.AccountStatus.ACTIVE,
                            BigDecimal.ZERO
                    ));
        }

        var result = mockMvc.perform(post("/accounts")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(expectedStatus.value()));

        if (expectedError != null) {
            result.andExpect(jsonPath("$.message").value(expectedError.message()));

            if (expectedError.fields() != null) {
                expectedError.fields().forEach((field, message) -> {
                    try {
                        result.andExpect(jsonPath("$.fields." + field).value(message));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        if (expectedError != null) {
            verify(accountService, never()).createAccount(any());
        }
    }

    @Test
    void should_return_400_when_accountType_is_invalid() throws Exception {
        String invalidJson = """
        {
          "documentNumber": "62.817.280/0001-82",
          "holderName": "John Doe",
          "email": "john@email.com",
          "accountType": "INVALID_TYPE"
        }
    """;

        mockMvc.perform(post("/accounts")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for accountType."));
    }
}
