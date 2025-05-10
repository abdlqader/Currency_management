package com.example.sanad.services.impl;

import com.example.sanad.entity.Currency;
import com.example.sanad.services.CurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServicesImpl implements CurrencyService {
    @Override
    public Page<Currency> listAllCurrencies(String searchQuery, Pageable pageable) {
        return null;
    }

    @Override
    public Currency getCurrencyByCode(String currencyCode) {
        return null;
    }

    @Override
    public Currency addCurrency(Currency currency) {
        return null;
    }
}
