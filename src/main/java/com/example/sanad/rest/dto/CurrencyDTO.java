package com.example.sanad.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record CurrencyDTO(UUID id, String name, CurrencyEnumDTO code, Instant createdAt) {}
