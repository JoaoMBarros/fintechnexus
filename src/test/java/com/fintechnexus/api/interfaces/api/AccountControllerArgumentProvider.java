package com.fintechnexus.api.interfaces.api;

import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.application.dto.ErrorResponseDTO;
import com.fintechnexus.api.domain.model.Account;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

public class AccountControllerArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context){
        return Stream.of(
                // ✅ Valid request → 201
                Arguments.of(
                        new AccountCreationDTO("62.817.280/0001-82", "John Doe", "john@email.com", Account.AccountType.CHECKING),
                        HttpStatus.CREATED,
                        null
                ),
                // ❌ Missing holderName → 400
                Arguments.of(
                        new AccountCreationDTO("62.817.280/0001-82", "", "john@email.com", Account.AccountType.CHECKING),
                        HttpStatus.BAD_REQUEST,
                        new ErrorResponseDTO("Invalid fields", Map.of("holderName", "must not be blank"), Instant.now())
                ),
                // ❌ Missing documentNumber → 400
                Arguments.of(
                        new AccountCreationDTO(null, "John Doe", "john@email.com", Account.AccountType.CHECKING),
                        HttpStatus.BAD_REQUEST,
                        new ErrorResponseDTO("Invalid fields", Map.of("documentNumber", "must not be blank"), Instant.now())
                ),
                // ❌ Missing accountType → 400
                Arguments.of(
                        new AccountCreationDTO("62.817.280/0001-82", "John Doe", "john@email.com", null),
                        HttpStatus.BAD_REQUEST,
                        new ErrorResponseDTO("Invalid fields", Map.of("accountType", "must not be null"), Instant.now())
                ),
                // ❌ Invalid cnpj → 400
                Arguments.of(
                        new AccountCreationDTO("1", "John Doe", "john@email.com", Account.AccountType.CHECKING),
                        HttpStatus.BAD_REQUEST,
                        new ErrorResponseDTO("Invalid fields", Map.of("documentNumber", "invalid Brazilian corporate taxpayer registry number (CNPJ)"), Instant.now())
                ),
                // ❌ Invalid email → 400
                Arguments.of(
                        new AccountCreationDTO("62.817.280/0001-82", "John Doe", "john", Account.AccountType.CHECKING),
                        HttpStatus.BAD_REQUEST,
                        new ErrorResponseDTO("Invalid fields", Map.of("email", "must be a well-formed email address"), Instant.now())
                )
        );
    }
}
