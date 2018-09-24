package com.fbieck.conf.batches;

import com.fbieck.batch.tweet.TweetProcessor;
import com.fbieck.batch.tweet.TweetReader;
import com.fbieck.batch.tweet.TweetWriter;
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
public class TweetBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TweetProcessor tweetProcessor;
    @Autowired
    private TweetReader tweetReader;
    @Autowired
    private TweetWriter tweetWriter;

    @Bean
    public Job job_tweet() {
        Step step = stepBuilderFactory.get("tweet")
                .<org.springframework.social.twitter.api.Tweet, Tweet>chunk(10)
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
}
