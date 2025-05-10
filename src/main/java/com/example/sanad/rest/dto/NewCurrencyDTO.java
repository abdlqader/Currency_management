package com.example.sanad.rest.dto;

public record NewCurrencyDTO(String name, CurrencyEnumDTO code) {
    public NewCurrencyDTO {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Name cannot be null or blank");
        }
        if (code == null) {
            throw new RuntimeException("Code cannot be null or blank");
        }
    }
}
