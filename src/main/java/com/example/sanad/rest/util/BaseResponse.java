package com.example.sanad.rest.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseResponse<T> extends BaseHttpResponse {
    private T data;

    public BaseResponse(T data) {
        this.data = data;
    }
}
