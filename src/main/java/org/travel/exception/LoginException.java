package org.travel.exception;

public class LoginException extends AuthException{

    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String PASSWORD_ERROR = "PASSWORD_ERROR";
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";
    public static final String XMPP_REGISTER_FAILED = "XMPP_LOGIN_FAILED";

    public LoginException(String errorCode, String message) {
        super(errorCode, message);
    }

    public static LoginException userNotFound() {
        return new LoginException(USER_NOT_FOUND,"用户名不存在");
    }

    public static LoginException passwordError(){
        return new LoginException(PASSWORD_ERROR,"密码错误");
    }

    public static LoginException accountDisabled(){
        return new LoginException(ACCOUNT_DISABLED,"账号已被禁用");
    }

    public static LoginException xmppLoginFailed(){
        return new LoginException(XMPP_REGISTER_FAILED,"聊天服登录失败，请稍后重试");
    }
}
