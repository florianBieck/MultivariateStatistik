package com.fbieck.components;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetLoader {

    @Autowired
    private Twitter twitter;

    private final SearchParameters searchParameters = new SearchParameters("#tesla")
            .lang("de")
            .count(1000);

    public List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<>();
        twitter.searchOperations().search(searchParameters)
                .getTweets().forEach(tweet -> {
            Tweet t = new Tweet();
            t.setId(tweet.getId());
            t.setCreator(tweet.getFromUser());
            t.setText(tweet.getText());
            tweets.add(t);
        });
        return tweets;
    }
}
