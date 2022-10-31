package com.wcreators.produceforexrate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "produce.http")
@Getter
@Setter
public class HttpProducerConfig {
    private String URL;
}
