package com.security.login.exception;

public class UserNotSavedException  extends  Exception{

    public UserNotSavedException(String message){
        super(message);
    }
    public UserNotSavedException(String message, Throwable cause){
        super(message, cause);
    }

}
