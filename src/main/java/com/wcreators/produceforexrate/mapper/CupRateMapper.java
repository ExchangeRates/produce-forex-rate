package com.wcreators.produceforexrate.mapper;

import com.wcreators.produceforexrate.dto.CupPointDTO;
import com.wcreators.produceforexrate.models.CupPoint;
import com.wcreators.produceforexrate.models.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CupRateMapper {

    CupPointDTO modelToDto(CupPoint model);

    @Mappings({
            @Mapping(target = "high", source = "sell"),
            @Mapping(target = "low", source = "sell"),
            @Mapping(target = "open", source = "sell"),
            @Mapping(target = "close", source = "sell"),
            @Mapping(target = "start", source = "createdDate"),
            @Mapping(target = "end", source = "createdDate")
    })
    CupPoint rateToCupPoint(Rate rate);
}
