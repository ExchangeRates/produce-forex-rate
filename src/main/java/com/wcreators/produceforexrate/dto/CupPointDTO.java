package com.wcreators.produceforexrate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CupPointDTO {
    private String major;
    private String minor;
    private double high;
    private double low;
    private double open;
    private double close;
    private Date start;
    private Date end;
}
