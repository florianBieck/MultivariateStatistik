package com.fbieck.batch.tweet;

import com.fbieck.entities.twitter.Tweet;
import org.joda.time.LocalDateTime;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class TweetProcessor implements ItemProcessor<org.springframework.social.twitter.api.Tweet, Tweet> {

    @Override
    public Tweet process(org.springframework.social.twitter.api.Tweet tweet) throws Exception {
        Tweet t = new Tweet();
        t.setId(tweet.getId());
        t.setCreator(tweet.getFromUser());
        t.setText(tweet.getText());
        t.setCreatedAt(LocalDateTime.fromDateFields(tweet.getCreatedAt()));
        t.setFavouriteCount(tweet.getFavoriteCount());
        t.setRetweetCount(tweet.getRetweetCount());
        t.setUserid((Long) tweet.getExtraData().get("custom_userid"));
        return t;
    }
}
