package com.fintechnexus.api.application.service;

import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.application.dto.AccountCreationResponseDTO;
import com.fintechnexus.api.domain.exception.AccountAlreadyExistsException;
import com.fintechnexus.api.domain.mappers.AccountMapper;
import com.fintechnexus.api.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountCreationResponseDTO createAccount(AccountCreationDTO accountData) {
        log.info("Creating account for document: {}", maskDocument(accountData.documentNumber()));

        if (accountRepository.existsByDocumentNumber(accountData.documentNumber())) {
            throw new AccountAlreadyExistsException(maskDocument(accountData.documentNumber()));
        }

        var accountCreated = accountRepository.save(accountMapper.toModel(accountData));
        return accountMapper.toDto(accountCreated);
    }

    private String maskDocument(String doc){
        if (doc == null || doc.length() < 4) return "****";
        return "*".repeat(doc.length() - 4) + doc.substring(doc.length() - 4);
    }
}
