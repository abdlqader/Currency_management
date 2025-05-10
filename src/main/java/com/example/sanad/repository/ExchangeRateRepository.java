package com.example.sanad.repository;

import com.example.sanad.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, UUID> {

}
