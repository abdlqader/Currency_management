package com.example.sanad.rest.dto;

import java.util.List;

public record CurrencyExchangeRateDTO(CurrencyEnumDTO code, String name, List<ExchangeRateDTO> rates) {
}
