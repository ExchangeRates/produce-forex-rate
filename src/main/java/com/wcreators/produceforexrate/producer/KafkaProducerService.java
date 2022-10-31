package com.wcreators.produceforexrate.producer;

import com.wcreators.produceforexrate.dto.CupPointDTO;
import com.wcreators.produceforexrate.mapper.CupRateMapper;
import com.wcreators.produceforexrate.models.CupPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "kafka.is-enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaProducerService implements ProducerService<CupPoint> {

    private final KafkaTemplate<String, CupPointDTO> template;
    private final CupRateMapper mapper;

    private String topicName(CupPoint model) {
        return String.format("parsed.CUP-%s-%s", model.getMajor(), model.getMinor());
    }

    @Override
    public void produce(CupPoint model) {
        CupPointDTO dto = mapper.modelToDto(model);
        template.send(topicName(model), dto);
    }
}
