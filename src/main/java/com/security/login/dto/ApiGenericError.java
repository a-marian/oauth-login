package com.security.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiGenericError {
    private HttpStatus httpStatus;
    private String code;
    private String type;
    private String title;
    private String message;
    private List<ApiSubError> subErrors;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    LocalDateTime timestamp;

    public ApiGenericError() {
        timestamp = LocalDateTime.now();
    }

    ApiGenericError(HttpStatus httpStatus){
        this();
        this.httpStatus = httpStatus;
    }

   public ApiGenericError(HttpStatus httpStatus, String type, Exception ex){
        this();
        this.httpStatus = httpStatus;
        this.type = type;
        this.title = ex.getMessage();
        this.message = ex.getLocalizedMessage();
    }

    public ApiGenericError(HttpStatus httpStatus, String type,
                           String title, List<ApiSubError> subErrors){
        this();
        this.httpStatus = httpStatus;
        this.type = type;
        this.title = title;
        this.subErrors = subErrors;
    }

}
