package org.travel.exception;

public class AuthException extends BusinessException{
    public AuthException(String errorCode,String message){
        super(errorCode,message);
    }
}
