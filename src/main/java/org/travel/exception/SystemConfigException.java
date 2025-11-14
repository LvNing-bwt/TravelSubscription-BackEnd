package org.travel.exception;

public class SystemConfigException extends BusinessException{
    public SystemConfigException(String message) {
        super("SYSTEM_CONFIG_ERROR", message);
    }
}
