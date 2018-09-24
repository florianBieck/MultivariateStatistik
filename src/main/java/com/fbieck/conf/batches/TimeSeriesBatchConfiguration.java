package com.fbieck.conf.batches;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.batch.timeseries.TimeSeriesProcessor;
import com.fbieck.batch.timeseries.TimeSeriesReader;
import com.fbieck.batch.timeseries.TimeSeriesWriter;
import com.fbieck.entities.alphavantage.TimeSeries;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeSeriesBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TimeSeriesProcessor timeSeriesProcessor;
    @Autowired
    private TimeSeriesReader timeSeriesReader;
    @Autowired
    private TimeSeriesWriter timeSeriesWriter;

    @Bean
    public Job job_timeseries() {
        Step step = stepBuilderFactory.get("timeseries")
                .<JsonNode, TimeSeries>chunk(10)
                .reader(timeSeriesReader)
                .processor(timeSeriesProcessor)
                .writer(timeSeriesWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_timeseries")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
