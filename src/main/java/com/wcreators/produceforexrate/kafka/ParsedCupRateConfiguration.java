package com.wcreators.produceforexrate.kafka;


import com.wcreators.produceforexrate.config.KafkaConfig;
import com.wcreators.produceforexrate.dto.CupPointDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ParsedCupRateConfiguration {

    private final KafkaConfig config;

    private Map<String, Object> producerFactoryDefaultConfig(KafkaConfig config) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                ProducerConfig.CLIENT_ID_CONFIG, config.getProducer().getClientId(),
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT",
                SaslConfigs.SASL_MECHANISM, "PLAIN",
                SaslConfigs.SASL_JAAS_CONFIG, String.format(
                        "%s required username=\"%s\" " + "password=\"%s\";", PlainLoginModule.class.getName(), "admin", "admin-secret"
                )
        );
    }

    @Bean
    public KafkaTemplate<String, CupPointDTO> kafkaCupRateParsedTemplate() {
        Map<String, Object> factoryConfig = producerFactoryDefaultConfig(config);
        ProducerFactory<String, CupPointDTO> producerFactory = new DefaultKafkaProducerFactory<>(factoryConfig);
        return new KafkaTemplate<>(producerFactory);
    }
}
