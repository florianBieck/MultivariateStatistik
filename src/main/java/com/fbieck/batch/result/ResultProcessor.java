package com.fbieck.batch.result;

import com.fbieck.entities.Result;
import com.fbieck.entities.Sentiment;
import com.fbieck.entities.StockChange;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.ResultRepository;
import com.fbieck.repository.SentimentRepository;
import com.fbieck.repository.StockChangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
public class ResultProcessor implements ItemProcessor<Tweet, Result> {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SentimentRepository sentimentRepository;
    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Value("#{jobParameters[hourinterval]}")
    private Integer hourinterval;

    @Override
    public Result process(Tweet tweet) throws Exception {

        Result result = null;
        try {
            result = resultRepository.findByTweetAndHourInterval(tweet, hourinterval);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("Not a unique Result: " + tweet.getId() + " " + hourinterval);
        }

        if (result == null) {
            result = new Result();
        }
        result.setHourInterval(hourinterval);

        result.setTweet(tweet);
        result.setFavouriteCount(tweet.getFavouriteCount());
        result.setRetweetCount(tweet.getRetweetCount());

        StockChange stockChange = null;
        try {
            stockChange = stockChangeRepository.findByTweetAndHourInterval(tweet, hourinterval);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("Not a unique Result: " + tweet.getId() + " " + hourinterval);
        }

        if (stockChange != null) {
            result.setChangeInterval((stockChange.getPriceEnd() / stockChange.getPriceStart()) - 1);
        } else {
            return null;
        }

        Sentiment sentiment = sentimentRepository.findByTweet(tweet);
        if (sentiment != null) {
            result.setPositivity(sentiment.getPositivity());
        } else {
            return null;
        }

        return result;
    }
}
