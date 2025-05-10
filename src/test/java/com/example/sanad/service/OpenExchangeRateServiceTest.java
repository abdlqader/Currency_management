package com.example.sanad.service;

import com.example.sanad.client.OpenExchangeRatesClient;
import com.example.sanad.client.dto.ExchangeRatesResponse;
import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.ExchangeRate;
import com.example.sanad.services.impl.OpenExchangeRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenExchangeRateServiceTest {

    @Mock
    private OpenExchangeRatesClient openExchangeRatesClient;

    @InjectMocks
    private OpenExchangeRateServiceImpl openExchangeRateService;

    private final String testAppId = "test-app-id";
    @BeforeEach
    void setUp() {
        // Manually set the appId since @Value injection doesn't work in tests
        openExchangeRateService = new OpenExchangeRateServiceImpl(openExchangeRatesClient, testAppId);
    }

    @Test
    void getExchangeRates_shouldReturnConvertedRatesForValidBaseCurrency() {
        // Arrange
        Currency baseCurrency = new Currency();
        baseCurrency.setCode(CurrencyEnum.AED); // UAE Dirham as base

        // Mock response from API (USD-based rates)
        ExchangeRatesResponse mockResponse = new ExchangeRatesResponse(
                "disclaimer", "license", 123456789L, "USD",
                Map.of(
                        "AED", BigDecimal.valueOf(3.6725),  // 1 USD = 3.6725 AED
                        "EUR", BigDecimal.valueOf(0.91),    // 1 USD = 0.91 EUR
                        "GBP", BigDecimal.valueOf(0.79)     // 1 USD = 0.79 GBP
                )
        );

        when(openExchangeRatesClient.getLatestRates(testAppId)).thenReturn(mockResponse);

        // Act
        List<ExchangeRate> result = openExchangeRateService.getExchangeRates(baseCurrency);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Should include all currencies

        // Verify conversion for EUR (0.91 / 3.6725 ≈ 0.2478)
        ExchangeRate eurRate = findRateForCurrency(result, CurrencyEnum.EUR);
        assertNotNull(eurRate);
        assertEquals(0, BigDecimal.valueOf(0.2478).compareTo(eurRate.getRate().setScale(4, BigDecimal.ROUND_HALF_UP)));

        // Verify conversion for GBP (0.79 / 3.6725 ≈ 0.2151)
        ExchangeRate gbpRate = findRateForCurrency(result, CurrencyEnum.GBP);
        assertNotNull(gbpRate);
        assertEquals(0, BigDecimal.valueOf(0.2151).compareTo(gbpRate.getRate().setScale(4, BigDecimal.ROUND_HALF_UP)));

        // Verify conversion for AED (should be 1.0)
        ExchangeRate aedRate = findRateForCurrency(result, CurrencyEnum.AED);
        assertNotNull(aedRate);
        assertEquals(0, BigDecimal.ONE.compareTo(aedRate.getRate()));

        verify(openExchangeRatesClient).getLatestRates(testAppId);
    }

    @Test
    void getExchangeRates_shouldThrowExceptionWhenBaseCurrencyRateIsMissing() {
        // Arrange
        Currency baseCurrency = new Currency();
        baseCurrency.setCode(CurrencyEnum.AFN); // Non-existent currency

        ExchangeRatesResponse mockResponse = new ExchangeRatesResponse(
                "disclaimer", "license", 123456789L, "USD",
                Map.of(
                        "AED", BigDecimal.valueOf(3.6725),
                        "EUR", BigDecimal.valueOf(0.91)
                )
        );

        when(openExchangeRatesClient.getLatestRates(testAppId)).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                        openExchangeRateService.getExchangeRates(baseCurrency),
                "Expected exception when base currency rate is missing"
        );
    }

    @Test
    void getExchangeRates_shouldThrowExceptionWhenBaseCurrencyRateIsZero() {
        // Arrange
        Currency baseCurrency = new Currency();
        baseCurrency.setCode(CurrencyEnum.AFN); // Currency with zero rate

        ExchangeRatesResponse mockResponse = new ExchangeRatesResponse(
                "disclaimer", "license", 123456789L, "USD",
                Map.of(
                        "ZERO", BigDecimal.ZERO, // Zero rate
                        "EUR", BigDecimal.valueOf(0.91)
                )
        );

        when(openExchangeRatesClient.getLatestRates(testAppId)).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                        openExchangeRateService.getExchangeRates(baseCurrency),
                "Expected exception when base currency rate is zero"
        );
    }

    @Test
    void getExchangeRates_shouldHandleDivisionPrecisely() {
        // Arrange
        Currency baseCurrency = new Currency();
        baseCurrency.setCode(CurrencyEnum.USD); // USD as base

        ExchangeRatesResponse mockResponse = new ExchangeRatesResponse(
                "disclaimer", "license", 123456789L, "USD",
                Map.of(
                        "USD", BigDecimal.ONE,    // 1 USD = 1 USD
                        "JPY", BigDecimal.valueOf(150.25) // 1 USD = 150.25 JPY
                )
        );

        when(openExchangeRatesClient.getLatestRates(testAppId)).thenReturn(mockResponse);

        // Act
        List<ExchangeRate> result = openExchangeRateService.getExchangeRates(baseCurrency);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // USD to USD should be 1.0
        ExchangeRate usdRate = findRateForCurrency(result, CurrencyEnum.USD);
        assertNotNull(usdRate);
        assertEquals(0, BigDecimal.ONE.compareTo(usdRate.getRate()));

        // JPY to USD should be 150.25 (since base is USD)
        ExchangeRate jpyRate = findRateForCurrency(result, CurrencyEnum.JPY);
        assertNotNull(jpyRate);
        assertEquals(0, BigDecimal.valueOf(150.25).compareTo(jpyRate.getRate()));
    }

    private ExchangeRate findRateForCurrency(List<ExchangeRate> rates, CurrencyEnum currency) {
        return rates.stream()
                .filter(rate -> rate.getTargetCurrencyCode() == currency)
                .findFirst()
                .orElse(null);
    }
}