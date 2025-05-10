package com.example.sanad.rest.dto;

import java.util.List;

public record CurrencyExchangeRateDTO(String code, String name, List<ExchangeRateDTO> rates) {
}
