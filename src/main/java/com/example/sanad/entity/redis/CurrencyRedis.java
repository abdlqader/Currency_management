package com.example.sanad.entity.redis;

import com.example.sanad.entity.CurrencyEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@RedisHash("currencies_redis")
@Data
public class CurrencyRedis {
    private UUID id;
    // The unique code of the currency, represented by a CurrencyEnum.
    // This code is used to identify the currency (e.g., USD for US Dollar, EUR for Euro).
    @Id
    private CurrencyEnum code;

    // The name of the currency, such as "US Dollar" or "Euro".
    // This is a human-readable name used to represent the currency in various contexts.
    private String name;

    // List of ExchangeRate entities where this currency is the source currency.
    // This represents all exchange rates where this currency is being converted into another currency.
    // For example, if the sourceCurrency is USD, it would list exchange rates for USD to EUR, USD to GBP, etc.
    private List<ExchangeRateRedis> exchangeRates;
    @Override
    public String toString() {
        return "Currency{" + "code='" + code + "}";
    }
}
