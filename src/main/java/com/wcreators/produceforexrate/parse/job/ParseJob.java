package com.wcreators.produceforexrate.parse.job;

import com.wcreators.produceforexrate.producer.ProducerService;
import com.wcreators.produceforexrate.models.CupPoint;
import com.wcreators.produceforexrate.models.Rate;
import com.wcreators.produceforexrate.service.cup.CupService;
import com.wcreators.produceforexrate.service.parse.forex.ParseRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParseJob {

    private final ParseRateService parseRateService;
    private final ProducerService<CupPoint> producerService;
    private final CupService cupService;

    @Scheduled(fixedDelay = 700)
    public void adaptEvent() {
        List<Rate> rates = parseRates();
        rates.stream()
                .map(cupService::addPoint)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(point -> log.debug("Parsed cup rate {}", point))
                .forEach(producerService::produce);
    }

    private List<Rate> parseRates() {
        try {
            return parseRateService.parse();
        } catch (Exception e) {
            log.warn("Exception when parsing {} {}", parseRateService.getResource().getName(), e.getMessage());
            parseRateService.reload();
        }
        return Collections.emptyList();
    }
}
