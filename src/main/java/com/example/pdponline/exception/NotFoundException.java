package com.example.pdponline.exception;

import com.example.pdponline.payload.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private ApiResponse apiResponse;
    private String message;

    public NotFoundException(String message) {
        this.message = message;
    }

    public NotFoundException(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }
}
