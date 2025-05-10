package com.example.sanad.services;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.ExchangeRate;

import java.util.List;

public interface OpenExchangeRateService {
    List<ExchangeRate> getExchangeRates(Currency baseCurrency);
}
