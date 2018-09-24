package com.fbieck.conf.batches;

import com.fbieck.batch.sentiment.SentimentProcessor;
import com.fbieck.batch.sentiment.SentimentReader;
import com.fbieck.batch.sentiment.SentimentWriter;
import com.fbieck.entities.Sentiment;
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
public class SentimentBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SentimentReader sentimentReader;
    @Autowired
    private SentimentProcessor sentimentProcessor;
    @Autowired
    private SentimentWriter sentimentWriter;

    @Bean
    public Job job_sentiment() {
        Step step = stepBuilderFactory.get("sentiment")
                .<Tweet, Sentiment>chunk(10)
                .reader(sentimentReader)
                .processor(sentimentProcessor)
                .writer(sentimentWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_sentiment")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
