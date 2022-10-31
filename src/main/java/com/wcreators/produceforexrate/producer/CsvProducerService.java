package com.wcreators.produceforexrate.producer;

import com.wcreators.produceforexrate.config.CsvProduerConfig;
import com.wcreators.produceforexrate.models.CupPoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@ConditionalOnProperty(value = "csv.is-enabled", havingValue = "true")
@RequiredArgsConstructor
public class CsvProducerService implements ProducerService<CupPoint> {

    private final CsvProduerConfig config;

    @SneakyThrows
    @Override
    public void produce(CupPoint model) {
        val bytes = String.format(
            "%f,%f,%f,%f%n",
            model.getOpen(),
            model.getClose(),
            model.getHigh(),
            model.getLow()
        ).getBytes(StandardCharsets.UTF_8);

        val path = Paths.get(config.getFile());
        Files.write(path, bytes, StandardOpenOption.APPEND);
    }
}
