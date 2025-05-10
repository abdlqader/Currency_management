package com.example.sanad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exchange_rates")
@EqualsAndHashCode(callSuper = true)
public class ExchangeRate extends AbstractEntity<ExchangeRate> {
    // Represents the source currency for which the exchange rate is defined.
    // For example, this could be USD if converting from USD to EUR.
    @ManyToOne
    private Currency sourceCurrency;

    // Represents the target currency to which the exchange rate is applied.
    // For example, this could be EUR if converting from USD to EUR.
    @Column(name = "target_currency_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyEnum targetCurrencyCode;

    // The exchange rate between the source and target currencies.
    // This value represents how much 1 unit of the source currency is worth in the target currency.
    // For instance, if the sourceCurrency is USD, and the targetCurrency is EUR,
    // this could be 0.85, meaning 1 USD is worth 0.85 EUR.
    @Column(name = "rate", nullable = false, precision = 38, scale = 30)
    private BigDecimal rate = BigDecimal.ZERO;
}
