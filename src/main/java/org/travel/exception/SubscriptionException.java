package org.travel.exception;

public class SubscriptionException extends BusinessException{

   public static final String NO_SUBSCRIPTION_PERMISSION = "NO_SUBSCRIPTION_PERMISSION";

    public SubscriptionException() {
        super("SUBSCRIPTION_ERROR", "订阅操作失败");
    }

    public SubscriptionException(String errorCode, String message){
        super(errorCode,message);
    }

    public static SubscriptionException noSubscriptionPermission(String provinceName) {
        return new SubscriptionException(NO_SUBSCRIPTION_PERMISSION,
                String.format("权限不足，无法访问%s的节点", provinceName));
    }
}
