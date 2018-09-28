package com.fbieck.conf.batches;

import com.fbieck.batch.regression.RegressionWriter;
import com.fbieck.batch.regression.h0.RegressionH0Processor;
import com.fbieck.batch.regression.h0.RegressionH0Reader;
import com.fbieck.batch.regression.h1.RegressionH1Processor;
import com.fbieck.batch.regression.h1.RegressionH1Reader;
import com.fbieck.batch.regression.h2.RegressionH2Processor;
import com.fbieck.batch.regression.h2.RegressionH2Reader;
import com.fbieck.entities.Regression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegressionBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private RegressionH0Processor regressionH0Processor;
    @Autowired
    private RegressionH0Reader regressionH0Reader;

    @Autowired
    private RegressionH1Processor regressionH1Processor;
    @Autowired
    private RegressionH1Reader regressionH1Reader;

    @Autowired
    private RegressionH2Processor regressionH2Processor;
    @Autowired
    private RegressionH2Reader regressionH2Reader;

    @Autowired
    private RegressionWriter regressionWriter;

    @Bean
    public Job job_regressionH0() {
        Step step = stepBuilderFactory.get("regressionH0")
                .<OLSMultipleLinearRegression, Regression>chunk(1)
                .reader(regressionH0Reader)
                .processor(regressionH0Processor)
                .writer(regressionWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_regressionH0")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Job job_regressionH1() {
        Step step = stepBuilderFactory.get("regressionH1")
                .<OLSMultipleLinearRegression, Regression>chunk(1)
                .reader(regressionH1Reader)
                .processor(regressionH1Processor)
                .writer(regressionWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_regressionH1")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Job job_regressionH2() {
        Step step = stepBuilderFactory.get("regressionH2")
                .<OLSMultipleLinearRegression, Regression>chunk(1)
                .reader(regressionH2Reader)
                .processor(regressionH2Processor)
                .writer(regressionWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_regressionH2")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
