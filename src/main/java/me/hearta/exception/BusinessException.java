package me.hearta.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException{
    private final String errorCode;
    private final String message;

    public BusinessException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
