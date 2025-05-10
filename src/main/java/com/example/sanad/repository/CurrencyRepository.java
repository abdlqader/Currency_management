package com.example.sanad.repository;

import com.example.sanad.entity.Currency;
import com.example.sanad.entity.CurrencyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findByCode(CurrencyEnum code);
}
