package com.fbieck.batch.result;

import com.fbieck.entities.Result;
import com.fbieck.entities.Sentiment;
import com.fbieck.entities.StockChange;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.ResultRepository;
import com.fbieck.repository.SentimentRepository;
import com.fbieck.repository.StockChangeRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
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

        Result result = resultRepository.findByTweetAndHourInterval(tweet, hourinterval);

        if (result == null) {
            result = new Result();
        }
        result.setHourInterval(hourinterval);

        result.setTweet(tweet);
        result.setFavouriteCount(tweet.getFavouriteCount());
        result.setRetweetCount(tweet.getRetweetCount());

        StockChange stockChange = stockChangeRepository.findByTweetAndHourInterval(tweet, hourinterval);
        if (stockChange != null) {
            result.setChangeInterval((stockChange.getPriceEnd() / stockChange.getPriceStart()) - 1);
        }

        Sentiment sentiment = sentimentRepository.findByTweet(tweet);
        if (sentiment != null) {
            result.setPositivity(sentiment.getPositivity());
        }

        return result;
    }
}
