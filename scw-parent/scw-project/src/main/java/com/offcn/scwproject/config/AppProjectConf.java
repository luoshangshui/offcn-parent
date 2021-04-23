package com.offcn.scwproject.config;

import com.offcn.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProjectConf {
    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OSSTemplate getOSSTemplate(){
        return new OSSTemplate();
    }
}
