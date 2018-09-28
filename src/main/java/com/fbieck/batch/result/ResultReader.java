package com.fbieck.batch.result;

import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.TweetRepository;
import com.google.common.collect.Lists;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultReader implements ItemReader<Tweet>, ItemStream {

    @Autowired
    private TweetRepository tweetRepository;

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
        tweets = Lists.newArrayList(tweetRepository.findAll());
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        tweets.clear();
    }
}
