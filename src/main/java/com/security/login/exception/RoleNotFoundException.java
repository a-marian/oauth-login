package com.security.login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends Exception {
    RoleNotFoundException(String message){
        super(message);
    }
    RoleNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
