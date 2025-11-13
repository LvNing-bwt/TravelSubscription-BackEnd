package org.travel.exception;

import lombok.extern.slf4j.Slf4j;
import org.travel.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Response<?>> handleLoginExceptions(LoginException e){
        log.warn("登录异常：{} - {}", e.getErrorCode(),e.getMessage());

        String userMessage;
        Response.Code code;
        switch ((e.getErrorCode())){
            case LoginException.USER_NOT_FOUND, LoginException.PASSWORD_ERROR -> {
                userMessage = "用户名或密码不存在";
                code = Response.Code.UNAUTHORIZED;
            }
//            case LoginException.ACCOUNT_DISABLED -> {
//                userMessage = e.getMessage();
//                code = Response.Code.FORBIDDEN;
//            }
            default -> {
                code = Response.Code.UNAUTHORIZED;
                userMessage = "登录失败";
            }
        }
        return ResponseEntity.ok().body(Response.error(code,userMessage));
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<Response<?>> handleRegisterException(RegisterException e){
        log.warn("注册异常：{} - {}", e.getErrorCode(),e.getMessage());

        String userMessage;
        Response.Code code;
        switch (e.getErrorCode()){
            case RegisterException.USERNAME_EXISTS -> {
                code = Response.Code.BAD_REQUEST;
                userMessage = e.getMessage();
            }
            case RegisterException.DATABASE_SAVE_FAILED, RegisterException.XMPP_REGISTER_FAILED -> {
                code = Response.Code.SERVER_ERROR;
                userMessage = e.getMessage();
            }
            default -> {
                code = Response.Code.BAD_REQUEST;
                userMessage = "注册失败";
            }
        }
        return ResponseEntity.ok().body(Response.error(code,userMessage));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleGeneralException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(Response.Code.SERVER_ERROR,"系统繁忙请稍后重试"));
    }
}
