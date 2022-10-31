package com.wcreators.produceforexrate.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcreators.produceforexrate.config.HttpProducerConfig;
import com.wcreators.produceforexrate.models.CupPoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@ConditionalOnProperty(value = "produce.http.is-enabled", havingValue = "true")
@RequiredArgsConstructor
public class HttpProducerService implements ProducerService<CupPoint> {

    private final HttpProducerConfig config;

    @SneakyThrows
    @Override
    public void produce(CupPoint model) {
        val objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(model);
        System.out.println(requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(config.getURL()))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
