package com.wcreators.produceforexrate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "forex")
@Getter
@Setter
public class ForexConfig {
    private ForexUser user;
}

