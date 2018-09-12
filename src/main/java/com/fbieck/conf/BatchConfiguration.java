package com.fbieck.conf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.batch.globalquote.GlobalQuoteProcessor;
import com.fbieck.batch.globalquote.GlobalQuoteReader;
import com.fbieck.batch.globalquote.GlobalQuoteWriter;
import com.fbieck.batch.timeseries.TimeSeriesProcessor;
import com.fbieck.batch.timeseries.TimeSeriesReader;
import com.fbieck.batch.timeseries.TimeSeriesWriter;
import com.fbieck.batch.tweet.TweetProcessor;
import com.fbieck.batch.tweet.TweetReader;
import com.fbieck.batch.tweet.TweetWriter;
import com.fbieck.entities.alphavantage.GlobalQuote;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private TweetProcessor tweetProcessor;
    @Autowired
    private TweetReader tweetReader;
    @Autowired
    private TweetWriter tweetWriter;

    @Autowired
    private GlobalQuoteProcessor globalQuoteProcessor;
    @Autowired
    private GlobalQuoteReader globalQuoteReader;
    @Autowired
    private GlobalQuoteWriter globalQuoteWriter;

    @Autowired
    private TimeSeriesProcessor timeSeriesProcessor;
    @Autowired
    private TimeSeriesReader timeSeriesReader;
    @Autowired
    private TimeSeriesWriter timeSeriesWriter;

    @Scheduled(fixedRate = 200000)
    private void launch_tweet() throws Exception {
        jobLauncher.run(job_tweet(), new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 200000)
    private void launch_globalquote() throws Exception {
        jobLauncher.run(job_globalquote(), new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 200)
    private void launch_timeseries() throws Exception {
        jobLauncher.run(job_timeseries(), new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Bean
    public Job job_tweet() {
        Step step = stepBuilderFactory.get("tweet")
                .<Tweet, Tweet>chunk(10)
                .reader(tweetReader)
                .processor(tweetProcessor)
                .writer(tweetWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_tweet")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

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
