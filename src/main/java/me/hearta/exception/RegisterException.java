package me.hearta.exception;

public class RegisterException extends AuthException{

    public static final String USERNAME_EXISTS = "USERNAME_EXISTS";
    public static final String DATABASE_SAVE_FAILED = "DATABASE_SAVE_FAILED";

    public RegisterException(String errorCode, String message) {
        super(errorCode, message);
    }

    public static RegisterException usernameExists() {
        return new RegisterException(USERNAME_EXISTS,"用户名已存在");
    }

    public static RegisterException databaseSaveFailed(){
        return new RegisterException(DATABASE_SAVE_FAILED,"系统繁忙，请稍后重试");
    }
}
