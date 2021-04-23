package com.offcn.scwproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.scwproject.mapper")
public class ScwProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScwProjectApplication.class);
    }
}
