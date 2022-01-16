package com.wcreators.produceforexrate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "selenium")
@Getter
@Setter
public class SeleniumConfig {
    private boolean headless;
    private int timeoutSec;
    private SeleniumDriver driver;
}
