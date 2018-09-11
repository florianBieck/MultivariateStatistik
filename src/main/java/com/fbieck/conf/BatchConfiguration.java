package com.fbieck.conf;

import com.fbieck.batch.TweetProcessor;
import com.fbieck.batch.TweetReader;
import com.fbieck.batch.TweetWriter;
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

    @Scheduled(fixedRate = 2000)
    private void launch_tweetsplit() throws Exception {
        jobLauncher.run(job_tweetsplit(), new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Bean
    public Job job_tweetsplit() {
        Step step = stepBuilderFactory.get("tweetsplit")
                .<Tweet, Tweet>chunk(10)
                .reader(tweetReader)
                .processor(tweetProcessor)
                .writer(tweetWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_tweetsplit")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }


}
