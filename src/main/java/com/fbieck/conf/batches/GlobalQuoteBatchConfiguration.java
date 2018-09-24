package com.fbieck.conf.batches;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.batch.globalquote.GlobalQuoteProcessor;
import com.fbieck.batch.globalquote.GlobalQuoteReader;
import com.fbieck.batch.globalquote.GlobalQuoteWriter;
import com.fbieck.entities.alphavantage.GlobalQuote;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalQuoteBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private GlobalQuoteProcessor globalQuoteProcessor;
    @Autowired
    private GlobalQuoteReader globalQuoteReader;
    @Autowired
    private GlobalQuoteWriter globalQuoteWriter;

    @Bean
    public Job job_globalquote() {
        Step step = stepBuilderFactory.get("globalquote")
                .<JsonNode, GlobalQuote>chunk(10)
                .reader(globalQuoteReader)
                .processor(globalQuoteProcessor)
                .writer(globalQuoteWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_globalquote")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
