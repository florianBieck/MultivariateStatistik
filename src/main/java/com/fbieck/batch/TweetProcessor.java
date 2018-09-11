package com.fbieck.batch;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TweetProcessor implements ItemProcessor<Tweet, Tweet> {
    @Override
    public Tweet process(Tweet tweet) throws Exception {
        tweet.setText(tweet.getText().toLowerCase());
        return tweet;
    }
}
