package com.fbieck.batch.timeseries;

import com.fbieck.entities.alphavantage.TimeSeries;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeSeriesWriter implements ItemWriter<TimeSeries> {
    @Override
    public void write(List<? extends TimeSeries> list) throws Exception {
        list.forEach(o -> System.out.println(o));
    }
}
