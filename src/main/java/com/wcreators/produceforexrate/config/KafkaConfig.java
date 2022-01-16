package com.wcreators.produceforexrate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
public class KafkaConfig {
    private String bootstrapServers;
    private KafkaProducerConfig producer;
}
