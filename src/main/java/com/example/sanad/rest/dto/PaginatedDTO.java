package com.example.sanad.rest.dto;

import lombok.Data;

import java.util.Optional;
@Data
public class PaginatedDTO {
    private Optional<Integer> currentPage = Optional.empty();

    private Optional<Integer> pageSize = Optional.empty();

    private Optional<Long> totalElements = Optional.empty();

    private Optional<Integer> totalPages = Optional.empty();
}
