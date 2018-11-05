package com.fbieck.conf.batches;

import com.fbieck.batch.stockchange.StockChangeProcessor;
import com.fbieck.batch.stockchange.StockChangeReader;
import com.fbieck.batch.stockchange.StockChangeWriter;
import com.fbieck.entities.StockChange;
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
public class StockChangeConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private StockChangeReader stockChangeReader;
    @Autowired
    private StockChangeProcessor stockChangeProcessor;
    @Autowired
    private StockChangeWriter stockChangeWriter;

    @Bean
    public Job job_stockchange() {
        Step step = stepBuilderFactory.get("stockchange")
                .<Tweet, StockChange>chunk(500)
                .reader(stockChangeReader)
                .processor(stockChangeProcessor)
                .writer(stockChangeWriter)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("job_stockchange")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
