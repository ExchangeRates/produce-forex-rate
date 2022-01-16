package com.wcreators.produceforexrate.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Data
@Builder
public class Rate {
    private String major;
    private String minor;

    private Double sell;
    private Double buy;

    private Date createdDate;
}
