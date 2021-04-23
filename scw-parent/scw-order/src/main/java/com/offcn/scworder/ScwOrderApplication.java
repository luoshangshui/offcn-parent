package com.offcn.scworder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.scworder.mapper")
@EnableFeignClients
@EnableCircuitBreaker
public class ScwOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScwOrderApplication.class);
    }
}
