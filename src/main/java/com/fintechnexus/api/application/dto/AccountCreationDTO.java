package com.fintechnexus.api.application.dto;

import com.fintechnexus.api.domain.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public record AccountCreationDTO(
        @NotBlank @CNPJ String documentNumber,
        @NotBlank String holderName,
        String email,
        @NotNull Account.AccountType accountType
) {
    public AccountCreationDTO {
        if (documentNumber != null) {
            documentNumber = documentNumber.replaceAll("[^0-9]", "");
        }
    }
}
