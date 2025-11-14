package org.travel.exception;

public class RegisterException extends AuthException{

    // ==================== 用户名校验相关 ====================
    public static final String USERNAME_EXISTS = "USERNAME_EXISTS";
    public static final String DATABASE_SAVE_FAILED = "DATABASE_SAVE_FAILED";

    // ==================== 公司信息校验相关 ====================
    public static final String COMPANY_CREDIT_CODE_EXISTS = "COMPANY_CREDIT_CODE_EXISTS";
    public static final String COMPANY_NAME_EXISTS = "COMPANY_NAME_EXISTS";

    // ==================== 系统异常相关 ====================
    public static final String XMPP_REGISTER_FAILED = "XMPP_REGISTER_FAILED";

    public RegisterException() {
        super("BAD_REQUEST", "注册失败");
    }
    public RegisterException(String errorCode,String message) {
        super(errorCode,message);
    }

    public static RegisterException usernameExists() {
        return new RegisterException(USERNAME_EXISTS,"用户名已存在");
    }

    public static RegisterException databaseSaveFailed(){
        return new RegisterException(DATABASE_SAVE_FAILED,"系统繁忙，请稍后重试");
    }

    public static RegisterException xmppRegisterFailed(){
        return new RegisterException(XMPP_REGISTER_FAILED,"聊天服务注册失败，请稍后重试");
    }

    // ==================== 公司信息校验异常 ====================
    public static RegisterException companyCreditCodeExists(String creditCode) {
        return new RegisterException(COMPANY_CREDIT_CODE_EXISTS,
                String.format("统一社会信用代码 '%s' 已被注册", creditCode));
    }

    public static RegisterException companyNameExists(String companyName) {
        return new RegisterException(COMPANY_NAME_EXISTS,
                String.format("该市已存在同名公司 '%s'", companyName));
    }
}
