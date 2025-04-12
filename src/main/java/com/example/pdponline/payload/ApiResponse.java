package com.example.pdponline.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    private final boolean success;
    private String message;
    private T data;
    private List<ErrorData> errors;


    //RESPONSE WITH BOOLEAN (SUCCESS OR FAIL)
    private ApiResponse() {
        this.success = true;
    }


    //SUCCESS RESPONSE WITH DATA
    private ApiResponse(T data) {
        this();
        this.data = data;
    }

    //SUCCESS RESPONSE WITH DATA AND MESSAGE
    private ApiResponse(T data, String message) {
        this();
        this.data = data;
        this.message = message;
    }

    //SUCCESS RESPONSE WITH MESSAGE
    private ApiResponse(String message, boolean msg) {
        this();
        this.message = message;
    }

    //ERROR RESPONSE WITH MESSAGE AND ERROR CODE
    private ApiResponse(String errorMsg, Integer errorCode) {
        this.success = false;
        this.errors = Collections.singletonList(new ErrorData(errorMsg, errorCode));
    }

    //ERROR RESPONSE WITH ERROR DATA LIST
    private ApiResponse(List<ErrorData> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <E> ApiResponse<E> successResponse(E data) {
        return new ApiResponse<>(data);
    }

    public static <E> ApiResponse<E> successResponse(E data, String message) {
        return new ApiResponse<>(data, message);
    }

    public static <E> ApiResponse<E> successResponse() {
        return new ApiResponse<>();
    }

    public static ApiResponse<String> successResponseForMsg(String message) {
        return new ApiResponse<>(message, true);
    }


    public static ApiResponse<ErrorData> errorResponse(String errorMsg, Integer errorCode) {
        return new ApiResponse<>(errorMsg, errorCode);
    }

    public static ApiResponse<ErrorData> errorResponse(List<ErrorData> errors) {
        return new ApiResponse<>(errors);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorData {
        //USERGA BORADIGAN XABAR
        private String errorMsg;

        //XATOLIK KODI
        private Integer errorCode;

        //QAYSI FIELD XATO EKANLIGI
        private String fieldName;

        public ErrorData(String errorMsg, Integer errorCode) {
            this.errorMsg = errorMsg;
            this.errorCode = errorCode;
        }
    }
}
