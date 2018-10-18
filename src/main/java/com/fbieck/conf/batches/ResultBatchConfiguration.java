package com.fbieck.conf.batches;

import com.fbieck.batch.result.ResultProcessor;
import com.fbieck.batch.result.ResultReader;
import com.fbieck.batch.result.ResultWriter;
import com.fbieck.entities.Result;
import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ResultProcessor resultProcessor;
    @Autowired
    private ResultReader resultReader;
    @Autowired
    private ResultWriter resultWriter;

    @Bean
    public Job job_result() {
        Step step = stepBuilderFactory.get("result")
                .<Tweet, Result>chunk(600)
                .reader(resultReader)
                .processor(resultProcessor)
                .writer(resultWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_result")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
