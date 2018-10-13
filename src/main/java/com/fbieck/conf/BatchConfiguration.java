package com.fbieck.conf;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Job job_tweet;
    @Autowired
    private Job job_timeseries;
    @Autowired
    private Job job_sentiment;
    @Autowired
    private Job job_stockchange;
    @Autowired
    private Job job_result;
    @Autowired
    private Job job_regressionH0;
    @Autowired
    private Job job_regressionH1;
    @Autowired
    private Job job_regressionH2;

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();

        return jobLauncher;
    }

    @Scheduled(fixedRate = 60000)
    private void launch_tweet() throws Exception {
        jobLauncher().run(job_tweet, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 60000)
    private void launch_timeseries() throws Exception {
        jobLauncher().run(job_timeseries, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 10000)
    private void launch_sentiment() throws Exception {
        jobLauncher().run(job_sentiment, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 10000)
    private void launch_stockchange_1h() throws Exception {
        jobLauncher().run(job_stockchange, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .addLong("hourinterval", 1L)
                .toJobParameters());
    }

    @Scheduled(fixedRate = 10000)
    private void launch_stockchange_24h() throws Exception {
        jobLauncher().run(job_stockchange, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .addLong("hourinterval", 24L)
                .toJobParameters());
    }

    @Scheduled(fixedRate = 10000)
    private void launch_stockchange_48h() throws Exception {
        jobLauncher().run(job_stockchange, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .addLong("hourinterval", 48L)
                .toJobParameters());
    }

    @Scheduled(fixedRate = 5000)
    private void launch_result() throws Exception {
        jobLauncher().run(job_result, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 5000)
    private void launch_regressionH0() throws Exception {
        jobLauncher().run(job_regressionH0, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 5000)
    private void launch_regressionH1() throws Exception {
        jobLauncher().run(job_regressionH1, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }

    @Scheduled(fixedRate = 5000)
    private void launch_regressionH2() throws Exception {
        jobLauncher().run(job_regressionH2, new JobParametersBuilder()
                .addLong("date", System.currentTimeMillis())
                .toJobParameters());
    }
}
