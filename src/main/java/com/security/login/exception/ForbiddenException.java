package com.security.login.exception;

import javax.naming.AuthenticationException;

public class ForbiddenException extends AuthenticationException {

    public ForbiddenException(String message){
        super(message);
    }
}
