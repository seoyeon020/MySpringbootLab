package com.rookies5.MySpringbootLab.runner;

import com.rookies5.MySpringbootLab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Value("${myprop.username}")
    private String username;

    @Autowired
    private MyPropProperties myPropProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Username from @Value: {}", username);

        logger.debug("Property Username: {}", myPropProperties.getUsername());
        logger.debug("Property Port: {}", myPropProperties.getPort());

    }
}
