package org.travel;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.travel.mapper")
public class TravelSubscription_BackEnd {
    public static void main(String[] args) {
        SpringApplication.run(TravelSubscription_BackEnd.class,args);
    }
}
