package com.example.sanad.rest.util;

import com.example.sanad.rest.dto.PaginatedDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginatedResponse<T> extends BaseResponse<T> {
    private PaginatedDTO pagination;

    public PaginatedResponse(T data) {
        super(data);
    }
}