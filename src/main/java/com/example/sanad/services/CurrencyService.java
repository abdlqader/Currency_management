package com.example.sanad.services;

import com.example.sanad.entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurrencyService {
    Page<Currency> listAllCurrencies(String searchQuery,Pageable pageable); // Get a list of all currencies with pagination
    Currency getCurrencyByCode(String currencyCode); // Get a currency by its code
    Currency addCurrency(Currency currency); // Add a new currency
    void updateExchangeRates();
}
