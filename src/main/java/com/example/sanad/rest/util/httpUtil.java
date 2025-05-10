package com.example.sanad.rest.util;

import com.example.sanad.rest.dto.PaginatedDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class httpUtil {
    public static <T> ResponseEntity<BaseResponse<T>> buildResponseEntity(HttpStatus status, String message, T data) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setData(data);
        response.setMessage(message);
        response.setStatus(status.value());
        return ResponseEntity.status(status).body(response);
    }
    public static <T> ResponseEntity<PaginatedResponse<T>> buildResponse(HttpStatus status, String message, Page<?> page, T data) {
        PaginatedResponse<T> response = new PaginatedResponse<T>(data);
        response.setMessage(message);
        response.setStatus(status.value());
        response.setPagination(buildPagination(page));
        return ResponseEntity.status(status).body(response);
    }
    public static PaginatedDTO buildPagination(Page<?> page) {
        PaginatedDTO pagination = new PaginatedDTO();
        pagination.setCurrentPage(Optional.of(page.getNumber()));
        pagination.setPageSize(Optional.of(page.getSize()));
        pagination.setTotalElements(Optional.of(page.getTotalElements()));
        pagination.setTotalPages(Optional.of(page.getTotalPages()));
        return pagination;
    }
    public static ResponseEntity<BaseHttpResponse> buildResponseEntity(HttpStatus status, String message) {
        BaseHttpResponse response = new BaseHttpResponse();
        response.setMessage(message);
        response.setStatus(status.value());
        return ResponseEntity.status(status).body(response);
    }
}
