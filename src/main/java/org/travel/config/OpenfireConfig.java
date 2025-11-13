package org.travel.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.openfire")
@Data
@Slf4j
public class OpenfireConfig {
    private String host;
    private int port;
    private String domain;
    private String securityMode = "disabled";
    private Admin admin = new Admin();

    @Data
    public static class Admin{
        private String username;
        private String password;
    }
}
