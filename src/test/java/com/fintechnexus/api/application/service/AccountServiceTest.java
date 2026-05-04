package com.fintechnexus.api.application.service;

import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.domain.mappers.AccountMapper;
import com.fintechnexus.api.domain.mappers.AccountMapperImpl;
import com.fintechnexus.api.domain.model.Account;
import com.fintechnexus.api.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void saveAccount(){
        when(accountMapper.toModel(any(AccountCreationDTO.class))).thenReturn(Account.builder()
                .documentNumber("00000000000000")
                .accountType(Account.AccountType.CHECKING)
                .email("Teste")
                .holderName("Teste")
                .build());

        accountService.createAccount(
                new AccountCreationDTO(
                        "00000000000000",
                        "Teste",
                        "Teste",
                        Account.AccountType.CHECKING
                )
        );

        verify(accountRepository).save(accountCaptor.capture());

        var savedAccount = accountCaptor.getValue();

        assertAll(
                () -> assertEquals("00000000000000", savedAccount.getDocumentNumber()),
                () -> assertEquals("Teste", savedAccount.getHolderName()),
                () -> assertEquals("Teste", savedAccount.getEmail()),
                () -> assertEquals(Account.AccountType.CHECKING, savedAccount.getAccountType()),
                () -> assertEquals(0L, savedAccount.getBalanceCents()),
                () -> assertEquals(Account.AccountStatus.ACTIVE, savedAccount.getStatus())
        );
    }
}
