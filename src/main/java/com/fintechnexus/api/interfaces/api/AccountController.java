package com.fintechnexus.api.interfaces.api;

import com.fintechnexus.api.application.dto.AccountCreationDTO;
import com.fintechnexus.api.application.dto.AccountCreationResponseDTO;
import com.fintechnexus.api.application.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreationResponseDTO> createAccount(@Valid @RequestBody AccountCreationDTO creationDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(creationDTO));
    }
}
