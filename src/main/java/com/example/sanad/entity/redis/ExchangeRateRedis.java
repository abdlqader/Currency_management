package com.example.sanad.entity.redis;

import com.example.sanad.entity.CurrencyEnum;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeRateRedis {
    private CurrencyEnum targetCurrencyCode;
    private BigDecimal rate;
}
