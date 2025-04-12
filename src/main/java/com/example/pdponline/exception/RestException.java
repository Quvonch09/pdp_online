package com.example.pdponline.exception;

import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String userMsg;
    private HttpStatus status;
    private List<ApiResponse.ErrorData> errors;

    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private ResponseError responseError;

    private Integer errorCode;


    public RestException(String userMsg, HttpStatus status) {
        super(userMsg);
        this.userMsg = userMsg;
        this.status = status;
    }
    private RestException(HttpStatus status, List<ApiResponse.ErrorData> errors) {
        this.status = status;
        this.errors = errors;
    }

    private RestException(String userMsg, int errorCode, HttpStatus status) {
        super(userMsg);
        this.errors = Collections.singletonList(new ApiResponse.ErrorData(userMsg, errorCode));
        this.userMsg = userMsg;
        this.status = status;
    }

    public static RestException restThrow(String userMsg, HttpStatus httpStatus) {
        return new RestException(userMsg, httpStatus);
    }

    private RestException(String resourceName, String fieldName, Object fieldValue, String userMsg) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.userMsg = String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue);
        this.status = HttpStatus.BAD_REQUEST;
//        this.errorCode = RestConstants.NO_ITEMS_FOUND;
    }

    public static RestException restThrow(String userMsg) {
        return new RestException(userMsg, HttpStatus.BAD_REQUEST);
    }

    public static RestException restThrow(ResponseError error) {
        return new RestException(error.getMessage(),HttpStatus.BAD_REQUEST);
    }

    public static RestException restThrow(List<ApiResponse.ErrorData> errors, HttpStatus status) {
        return new RestException(status, errors);
    }

    public static RestException restThrow(String userMsg, int errorCode, HttpStatus httpStatus) {

        return new RestException(userMsg, errorCode, httpStatus);
    }

    public static RestException restThrow(String resourceName, String fieldName, Object fieldValue, String userMsg) {
        return new RestException(resourceName, fieldName, fieldValue, userMsg);
    }
}
