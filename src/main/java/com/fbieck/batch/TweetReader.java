package com.fbieck.batch;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TweetReader implements ItemReader<Tweet>, ItemStream {

    private final SearchParameters searchParameters = new SearchParameters("#tesla")
            .lang("de")
            .count(3);
    @Autowired
    private Twitter twitter;
    private List<Tweet> tweets;

    @Override
    public Tweet read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!tweets.isEmpty()) {
            return tweets.remove(0);
        }
        return null;
    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        List<Tweet> tweets = new ArrayList<>();
        twitter.searchOperations().search(searchParameters)
                .getTweets().forEach(tweet -> {
            Tweet t = new Tweet();
            t.setId(tweet.getId());
            t.setCreator(tweet.getFromUser());
            t.setText(tweet.getText());
            tweets.add(t);
        });
        this.tweets = tweets;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        tweets.clear();
    }
}
