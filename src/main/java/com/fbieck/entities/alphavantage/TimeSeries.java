package com.fbieck.entities.alphavantage;

import lombok.Data;

import java.util.List;

@Data
public class TimeSeries {

    private String information;

    private String symbol;

    private String lastRefreshed;

    private String interval;

    private String outputsize;

    private String timezone;

    private List<TimeSeriesEntry> entries;
}
