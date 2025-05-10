package com.example.sanad.repository.redis;

import com.example.sanad.entity.CurrencyEnum;
import com.example.sanad.entity.redis.CurrencyRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyRedisRepository extends CrudRepository<CurrencyRedis, CurrencyEnum> {
    Optional<CurrencyRedis> findByCode(CurrencyEnum code);
}
