package com.example.sanad.controller;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.rest.CurrencyController;
import com.example.sanad.rest.dto.*;
import com.example.sanad.rest.mapper.CurrencyMapper;
import com.example.sanad.services.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CurrencyController.class)
@Import(CurrencyControllerTest.Config.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Currency currency;
    private CurrencyDTO currencyDTO;
    private NewCurrencyDTO newCurrencyDTO;

    @TestConfiguration
    static class Config {
        @Bean
        public CurrencyService currencyService() {
            return Mockito.mock(CurrencyService.class);
        }

        @Bean
        public CurrencyMapper currencyMapper() {
            return Mockito.mock(CurrencyMapper.class);
        }
    }

    @BeforeEach
    void setup() {
        currency = new Currency(CurrencyEnum.AED,"United Arab Emirates Dirham", Collections.emptyList());
        currencyDTO = new CurrencyDTO(UUID.randomUUID(),"United Arab Emirates Dirham", CurrencyEnumDTO.AED, Instant.now());
        newCurrencyDTO = new NewCurrencyDTO("United Arab Emirates Dirham", CurrencyEnumDTO.AED);
    }

    @Test
    void getAllCurrencies_shouldReturnPage() throws Exception {
        Page<Currency> page = new PageImpl<>(List.of(currency));
        Mockito.when(currencyService.listAllCurrencies(anyString(), any(Pageable.class)))
                .thenReturn(page);
        Mockito.when(currencyMapper.toDto(any(Currency.class))).thenReturn(currencyDTO);

        mockMvc.perform(get("/api/v1/currency")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "name")
                        .param("searchQuery", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Currencies retrieved successfully"));
    }

    @Test
    void getExchangeRates_shouldReturnDto() throws Exception {
        CurrencyExchangeRateDTO exchangeRateDTO = new CurrencyExchangeRateDTO(CurrencyEnumDTO.AED, "United Arab Emirates Dirham", List.of(new ExchangeRateDTO(CurrencyEnumDTO.USD, BigDecimal.valueOf(3.67))));
        Mockito.when(currencyService.getCurrencyByCode("USD")).thenReturn(currency);
        Mockito.when(currencyMapper.toExchangeRateDto(currency)).thenReturn(exchangeRateDTO);

        mockMvc.perform(get("/api/v1/currency/USD/exchange-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Exchange rates retrieved successfully"));
    }

    @Test
    void addCurrency_shouldReturnCreated() throws Exception {
        Mockito.when(currencyMapper.toEntity(any(NewCurrencyDTO.class))).thenReturn(currency);
        Mockito.when(currencyService.addCurrency(any(Currency.class))).thenReturn(currency);
        Mockito.when(currencyMapper.toDto(currency)).thenReturn(currencyDTO);

        mockMvc.perform(post("/api/v1/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCurrencyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Currency added successfully"));
    }
}
