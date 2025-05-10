package com.example.sanad.service;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.ExchangeRate;
import com.example.sanad.entity.redis.CurrencyRedis;
import com.example.sanad.repository.CurrencyRepository;
import com.example.sanad.repository.redis.CurrencyRedisRepository;
import com.example.sanad.rest.mapper.CurrencyMapper;
import com.example.sanad.services.impl.CurrencyServicesImpl;
import com.example.sanad.services.impl.OpenExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRedisRepository currencyRedisRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private OpenExchangeRateServiceImpl openExchangeRateServiceImpl;

    @InjectMocks
    private CurrencyServicesImpl currencyServices;

    @Test
    void listAllCurrencies_shouldReturnPageOfCurrencies() {
        // Arrange
        String searchQuery = "USD";
        Pageable pageable = mock(Pageable.class);
        Currency currency = new Currency();
        currency.setCode(CurrencyEnum.USD);
        Page<Currency> expectedPage = new PageImpl<>(Collections.singletonList(currency));

        when(currencyRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(expectedPage);

        // Act
        Page<Currency> result = currencyServices.listAllCurrencies(searchQuery, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(CurrencyEnum.USD, result.getContent().get(0).getCode());
        verify(currencyRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getCurrencyByCode_shouldReturnFromRedisWhenAvailable() {
        // Arrange
        String currencyCode = "USD";
        CurrencyRedis currencyRedis = new CurrencyRedis();
        currencyRedis.setCode(CurrencyEnum.USD);
        Currency expectedCurrency = new Currency();
        expectedCurrency.setCode(CurrencyEnum.USD);

        when(currencyRedisRepository.findByCode(CurrencyEnum.valueOf(currencyCode)))
                .thenReturn(Optional.of(currencyRedis));
        when(currencyMapper.toEntity(currencyRedis)).thenReturn(expectedCurrency);

        // Act
        Currency result = currencyServices.getCurrencyByCode(currencyCode);

        // Assert
        assertNotNull(result);
        assertEquals(CurrencyEnum.USD, result.getCode());
        verify(currencyRedisRepository).findByCode(CurrencyEnum.USD);
        verify(currencyMapper).toEntity(currencyRedis);
        verifyNoInteractions(currencyRepository);
    }

    @Test
    void getCurrencyByCode_shouldReturnFromDbAndCacheWhenNotInRedis() {
        // Arrange
        String currencyCode = "USD";
        Currency currencyFromDb = new Currency();
        currencyFromDb.setCode(CurrencyEnum.USD);
        CurrencyRedis currencyRedis = new CurrencyRedis();
        currencyRedis.setCode(CurrencyEnum.USD);

        when(currencyRedisRepository.findByCode(CurrencyEnum.valueOf(currencyCode)))
                .thenReturn(Optional.empty());
        when(currencyRepository.findByCode(CurrencyEnum.valueOf(currencyCode)))
                .thenReturn(Optional.of(currencyFromDb));
        when(currencyMapper.toRedis(currencyFromDb)).thenReturn(currencyRedis);

        // Act
        Currency result = currencyServices.getCurrencyByCode(currencyCode);

        // Assert
        assertNotNull(result);
        assertEquals(CurrencyEnum.USD, result.getCode());
        verify(currencyRedisRepository).findByCode(CurrencyEnum.USD);
        verify(currencyRepository).findByCode(CurrencyEnum.USD);
        verify(currencyMapper).toRedis(currencyFromDb);
        verify(currencyRedisRepository).save(currencyRedis);
    }

    @Test
    void getCurrencyByCode_shouldThrowExceptionWhenCurrencyNotFound() {
        // Arrange
        CurrencyEnum currencyCode = CurrencyEnum.AFN;

        when(currencyRedisRepository.findByCode(currencyCode))
                .thenReturn(Optional.empty());
        when(currencyRepository.findByCode(currencyCode))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> currencyServices.getCurrencyByCode(currencyCode.toString()));
        verify(currencyRedisRepository).findByCode(CurrencyEnum.AFN);
        verify(currencyRepository).findByCode(CurrencyEnum.AFN);
    }

    @Test
    void addCurrency_shouldReturnExistingCurrencyWhenAlreadyExists() {
        // Arrange
        Currency currency = new Currency();
        currency.setCode(CurrencyEnum.USD);

        when(currencyRepository.findByCode(currency.getCode()))
                .thenReturn(Optional.of(currency));

        // Act
        Currency result = currencyServices.addCurrency(currency);

        // Assert
        assertNotNull(result);
        assertEquals(CurrencyEnum.USD, result.getCode());
        verify(currencyRepository).findByCode(CurrencyEnum.USD);
        verifyNoMoreInteractions(currencyRepository);
        verifyNoInteractions(openExchangeRateServiceImpl);
        verifyNoInteractions(currencyRedisRepository);
    }

    @Test
    void addCurrency_shouldSaveNewCurrencyWithExchangeRates() {
        // Arrange
        Currency currency = new Currency();
        currency.setCode(CurrencyEnum.USD);
        ExchangeRate exchangeRate = new ExchangeRate();
        List<ExchangeRate> exchangeRates = Collections.singletonList(exchangeRate);
        Currency savedCurrency = new Currency();
        savedCurrency.setCode(CurrencyEnum.USD);
        savedCurrency.setExchangeRates(exchangeRates);
        CurrencyRedis currencyRedis = new CurrencyRedis();
        currencyRedis.setCode(CurrencyEnum.USD);

        when(currencyRepository.findByCode(currency.getCode()))
                .thenReturn(Optional.empty());
        when(openExchangeRateServiceImpl.getExchangeRates(currency))
                .thenReturn(exchangeRates);
        when(currencyRepository.save(currency))
                .thenReturn(savedCurrency);
        when(currencyMapper.toRedis(savedCurrency))
                .thenReturn(currencyRedis);

        // Act
        Currency result = currencyServices.addCurrency(currency);

        // Assert
        assertNotNull(result);
        assertEquals(CurrencyEnum.USD, result.getCode());
        assertEquals(1, result.getExchangeRates().size());
        verify(currencyRepository).findByCode(CurrencyEnum.USD);
        verify(openExchangeRateServiceImpl).getExchangeRates(currency);
        verify(currencyRepository).save(currency);
        verify(currencyMapper).toRedis(savedCurrency);
        verify(currencyRedisRepository).save(currencyRedis);
    }

}
