package com.example.sanad.services.impl;

import com.example.sanad.client.OpenExchangeRatesClient;
import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenExchangeRateServiceImpl implements com.example.sanad.services.OpenExchangeRateService {
    private final OpenExchangeRatesClient openExchangeRatesClient;
    private final String appId;

    public OpenExchangeRateServiceImpl(OpenExchangeRatesClient openExchangeRatesClient, @Value("${openexchangerates.app-id}") String appId) {
        this.openExchangeRatesClient = openExchangeRatesClient;
        this.appId = appId;
    }

    @Override
    public List<ExchangeRate> getExchangeRates(Currency baseCurrency) {
        // Step 1: Fetch latest exchange rates from Open Exchange Rates API.
        // Rates are in terms of USD (i.e., USD → currency).
        Map<String, BigDecimal> usdRates = openExchangeRatesClient.getLatestRates(appId).rates();

        // Step 2: Get the rate of the base currency in terms of USD.
        // This is used as the divisor to convert all other currencies to baseCurrency equivalents.
        BigDecimal targetBaseRate = usdRates.get(baseCurrency.getCode().name());

        // Step 3: Validate the target base rate.
        // If the rate is missing or zero, we cannot convert accurately, so we throw an exception.
        if (targetBaseRate == null || targetBaseRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid or missing rate for base currency: " + baseCurrency);
        }

        // Step 4: Prepare a list to collect the converted exchange rates.
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        // Step 5: Loop through each USD-based rate in the response.
        for (Map.Entry<String, BigDecimal> entry : usdRates.entrySet()) {
            String currencyCode = entry.getKey(); // Example: "EUR"
            BigDecimal usdToCurrencyRate = entry.getValue(); // Example: 0.91

            // Step 6: Convert USD-based rate to baseCurrency-based rate.
            // Formula: (USD → currency) / (USD → baseCurrency) = baseCurrency → currency
            // Example: EUR / AED = how many EUR per 1 AED
            BigDecimal targetBaseToCurrencyRate = usdToCurrencyRate.divide(targetBaseRate, 10, RoundingMode.HALF_UP);

            // Step 7: Create and store a new ExchangeRate object.
            exchangeRates.add(new ExchangeRate(
                    baseCurrency,
                    CurrencyEnum.valueOf(currencyCode),
                    targetBaseToCurrencyRate
            ));
        }

        // Step 8: Return the full list of exchange rates relative to the base currency.
        return exchangeRates;
    }
}
