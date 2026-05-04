package com.fintechnexus.api.application.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDTO(
        String message,
        Map<String, String> fields,
        Instant timestamp
) {
    public static ErrorResponseDTO of(String message){
        return new ErrorResponseDTO(message, null, Instant.now());
    }
}
