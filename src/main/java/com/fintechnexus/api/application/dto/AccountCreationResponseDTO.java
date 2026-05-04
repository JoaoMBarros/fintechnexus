package com.fintechnexus.api.application.dto;

import com.fintechnexus.api.domain.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountCreationResponseDTO(
        UUID id,
        String documentNumber,
        String holderName,
        Account.AccountType accountType,
        Account.AccountStatus accountStatus,
        BigDecimal balance
) {
}
