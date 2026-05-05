package com.fintechnexus.api.domain.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String maskedDocument) {
        super("Account already exists for document: " + maskedDocument);
    }
}