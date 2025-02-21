package com.duongw.common.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Data
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
