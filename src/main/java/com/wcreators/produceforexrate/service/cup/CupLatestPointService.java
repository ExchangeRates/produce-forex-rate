package com.wcreators.produceforexrate.service.cup;

import com.wcreators.produceforexrate.mapper.CupRateMapper;
import com.wcreators.produceforexrate.models.CupPoint;
import com.wcreators.produceforexrate.models.Rate;
import com.wcreators.produceforexrate.service.date.DateProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CupLatestPointService implements CupService {
    private CupPoint current;
    private final DateProcessingService<Date> dateProcessingService;
    private final CupRateMapper mapper;

    @Override
    public Optional<CupPoint> addPoint(Rate rate) {
        if (current == null) {
            current = mapper.rateToCupPoint(rate);
            return Optional.empty();
        }

        if (dateProcessingService.getMinutes(current.getStart()) == dateProcessingService.getMinutes(rate.getCreatedDate())) {
            current.addPrice(rate.getSell(), rate.getCreatedDate());
            return Optional.empty();
        }

        CupPoint addedPoint = current;
        current = mapper.rateToCupPoint(rate);

        return Optional.of(addedPoint);
    }
}
