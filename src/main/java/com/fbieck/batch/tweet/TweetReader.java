package com.fbieck.batch.tweet;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class TweetReader implements ItemReader<Tweet>, ItemStream {

    private final SearchParameters searchParameters = new SearchParameters("#tesla")
            .lang("en")
            .count(1000);
    @Value("${custom.hourfilter}")
    private Integer hourfilter;

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
            t.setCreatedAt(tweet.getCreatedAt());
            t.setFavouriteCount(tweet.getFavoriteCount());
            t.setRetweetCount(tweet.getRetweetCount());

            LocalDateTime localDateTime = LocalDateTime.now().minus(hourfilter, ChronoUnit.HOURS);
            if (localDateTime
                    .compareTo(LocalDateTime.ofInstant(tweet.getCreatedAt().toInstant(), ZoneId.systemDefault())) < 0) {
                tweets.add(t);
            } else {
                System.out.println("OUT OF FILTER " + tweet.getCreatedAt());
            }
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
