package com.example.sanad.rest.dto;

import java.math.BigDecimal;

public record ExchangeRateDTO(CurrencyEnumDTO targetCurrencyCode, BigDecimal rate) {
}
