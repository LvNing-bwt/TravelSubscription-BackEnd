package me.hearta;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
@MapperScan("me.hearta.mapper")
public class TravelSubscription_BackEnd {
    public static void main(String[] args) {
        SpringApplication.run(TravelSubscription_BackEnd.class,args);
    }
}
