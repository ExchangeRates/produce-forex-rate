package com.wcreators.produceforexrate.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Builder
@Data
public class CupPoint {

    private String major;
    private String minor;
    private Double high;
    private Double low;
    private Double open;
    private Double close;
    private Date start;
    private Date end;

    public void addPrice(double price, Date current) {
        close = price;
        end = current;
        high = max(high, close);
        low = min(low, close);
    }
}
