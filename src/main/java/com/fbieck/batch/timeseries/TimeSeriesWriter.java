package com.fbieck.batch.timeseries;

import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.repository.TimeSeriesEntryRepository;
import com.fbieck.repository.TimeSeriesRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeSeriesWriter implements ItemWriter<TimeSeries> {

    @Autowired
    private TimeSeriesRepository timeSeriesRepository;
    @Autowired
    private TimeSeriesEntryRepository timeSeriesEntryRepository;

    @Override
    public void write(List<? extends TimeSeries> list) throws Exception {
        list.forEach(timeseries -> {
            timeSeriesEntryRepository.saveAll(timeseries.getEntries());
            timeSeriesRepository.save(timeseries);
        });
    }
}
