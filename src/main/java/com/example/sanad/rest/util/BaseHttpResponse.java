package com.example.sanad.rest.util;

import lombok.Data;

@Data
public class BaseHttpResponse {
    private int status;
    private String message;
}