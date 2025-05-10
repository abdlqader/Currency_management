package com.example.sanad.rest.mapper;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.redis.CurrencyRedis;
import com.example.sanad.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDTO toDto(Currency currency);
    Currency toEntity(NewCurrencyDTO newCurrencyDTO);
    @Mapping(target = "rates", expression = "java(mapRates(currency))")
    CurrencyExchangeRateDTO toExchangeRateDto(Currency currency);
    Currency toEntity(CurrencyRedis redis);
    CurrencyRedis toRedis(Currency currency);

    default List<ExchangeRateDTO> mapRates(Currency currency) {
        return currency.getExchangeRates().stream()
                .map(rate -> new ExchangeRateDTO(toCurrencyEnumDTO(rate.getTargetCurrencyCode()), rate.getRate()))
                .toList();
    }
    default CurrencyEnumDTO toCurrencyEnumDTO(CurrencyEnum currency) {
        return  CurrencyEnumDTO.valueOf(currency.toString());
    }
}
