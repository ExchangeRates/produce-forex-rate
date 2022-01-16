package com.wcreators.produceforexrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProduceForexRateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduceForexRateApplication.class, args);
    }

}
