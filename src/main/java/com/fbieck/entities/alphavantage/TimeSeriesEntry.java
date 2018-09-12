package com.fbieck.entities.alphavantage;

import lombok.Data;

@Data
public class TimeSeriesEntry {

    private String timestamp;

    private Double open;

    private Double high;

    private Double low;

    private Double close;

    private Double volume;
}
