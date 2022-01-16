package com.wcreators.produceforexrate.kafka;

import com.wcreators.produceforexrate.dto.CupPointDTO;
import com.wcreators.produceforexrate.mapper.CupRateMapper;
import com.wcreators.produceforexrate.models.CupPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParsedCupRateProducerService implements ProducerService<CupPoint> {

    private final KafkaTemplate<String, CupPointDTO> template;
    private final CupRateMapper mapper;

    @Override
    public String topicName(CupPoint model) {
        return String.format("parsed.CUP-%s-%s", model.getMajor(), model.getMinor());
    }

    @Override
    public void produce(CupPoint model) {
        CupPointDTO dto = mapper.modelToDto(model);
        template.send(topicName(model), dto);
    }
}
