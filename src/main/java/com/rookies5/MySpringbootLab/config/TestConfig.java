package com.rookies5.MySpringbootLab.config;

import com.rookies5.MySpringbootLab.model.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder().mode("개발환경").build();
    }
}
