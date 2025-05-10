package com.example.sanad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currencies")
@EqualsAndHashCode(callSuper = true)
public class Currency extends AbstractEntity<Currency> {
    // The unique code of the currency, represented by a CurrencyEnum.
    // This code is used to identify the currency (e.g., USD for US Dollar, EUR for Euro).
    @Column(name = "code", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CurrencyEnum code;

    // The name of the currency, such as "US Dollar" or "Euro".
    // This is a human-readable name used to represent the currency in various contexts.
    @Column(name = "name", nullable = false)
    private String name;

    // List of ExchangeRate entities where this currency is the source currency.
    // This represents all exchange rates where this currency is being converted into another currency.
    // For example, if the sourceCurrency is USD, it would list exchange rates for USD to EUR, USD to GBP, etc.
    @OneToMany(mappedBy = "sourceCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExchangeRate> exchangeRates;

    @Override
    public String toString() {
        return "Currency{" + "id=" + id +", code='" + code + "}";
    }
}
