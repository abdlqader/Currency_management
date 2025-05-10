package com.example.sanad.services.impl;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.ExchangeRate;
import com.example.sanad.entity.redis.CurrencyRedis;
import com.example.sanad.repository.CurrencyRepository;
import com.example.sanad.repository.redis.CurrencyRedisRepository;
import com.example.sanad.rest.mapper.CurrencyMapper;
import com.example.sanad.services.CurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServicesImpl implements CurrencyService {
    private final CurrencyRedisRepository currencyRedisRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final OpenExchangeRateService openExchangeRateService;

    public CurrencyServicesImpl(CurrencyRedisRepository currencyRedisRepository, CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, OpenExchangeRateService openExchangeRateService) {
        this.currencyRedisRepository = currencyRedisRepository;
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.openExchangeRateService = openExchangeRateService;
    }

    @Override
    public Page<Currency> listAllCurrencies(String searchQuery, Pageable pageable) {
        Specification<Currency> spec = Specification.where(withSearchQuery(searchQuery));
        return currencyRepository.findAll(spec, pageable);
    }

    @Override
    public Currency getCurrencyByCode(String currencyCode) {
        // Try Redis first
        Optional<CurrencyRedis> cached = currencyRedisRepository.findByCode(CurrencyEnum.valueOf(currencyCode));
        if (cached.isPresent()) {
            CurrencyRedis cr = cached.get();
            return currencyMapper.toEntity(cr); // Map back to entity or DTO
        }

        // Fallback to DB
        Optional<Currency> currencyFromDb = currencyRepository.findByCode(CurrencyEnum.valueOf(currencyCode));
        if (currencyFromDb.isPresent()) {
            Currency currency = currencyFromDb.get();

            // Add to Redis for next time
            currencyRedisRepository.save(currencyMapper.toRedis(currency));

            return currency;
        }

        // If not found in both, return null or throw an exception
        throw new RuntimeException("Currency not found");
    }

    @Override
    public Currency addCurrency(Currency currency) {
        // Check if currency already exists
        Optional<Currency> existingCurrency = currencyRepository.findByCode(currency.getCode());
        if(existingCurrency.isPresent()) {
            return existingCurrency.get();
        }

        // Fetch exchange rates from OpenExchangeRateService
        List<ExchangeRate> exchangeRates = openExchangeRateService.getExchangeRates(currency);
        currency.setExchangeRates(exchangeRates);

        // Save to DB
        Currency savedCurrency = currencyRepository.save(currency);

        // Save to Redis
        currencyRedisRepository.save(currencyMapper.toRedis(savedCurrency));

        return savedCurrency;
    }

    private Specification<Currency> withSearchQuery(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) return null;
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("code")), "%" + searchQuery.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("name")), "%" + searchQuery.toLowerCase() + "%")
        );
    }
}
