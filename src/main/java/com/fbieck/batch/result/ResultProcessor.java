package com.fbieck.batch.result;

import com.fbieck.entities.Result;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.ResultRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultProcessor implements ItemProcessor<Tweet, Result> {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public Result process(Tweet tweet) throws Exception {

        Result result = resultRepository.findByTweet(tweet);

        if (result == null) {
            result = new Result();
        }

        result.setTweet(tweet);
        result.setFavouriteCount(tweet.getFavouriteCount());
        result.setRetweetCount(tweet.getRetweetCount());

        if (tweet.getStockChange() != null) {
            result.setHourInterval(tweet.getStockChange().getHourInterval());
            result.setChangeInterval((tweet.getStockChange().getPriceEnd() / tweet.getStockChange().getPriceStart()) - 1);
        }

        if (tweet.getSentiment() != null) {
            result.setPositivity(tweet.getSentiment().getPositivity());
        }

        return result;
    }
}
