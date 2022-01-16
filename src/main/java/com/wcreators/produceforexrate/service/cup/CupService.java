package com.wcreators.produceforexrate.service.cup;

import com.wcreators.produceforexrate.models.CupPoint;
import com.wcreators.produceforexrate.models.Rate;

import java.util.Optional;

public interface CupService {

    Optional<CupPoint> addPoint(Rate value);
}
