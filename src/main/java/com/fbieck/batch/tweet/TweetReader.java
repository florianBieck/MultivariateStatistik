package com.fbieck.batch.tweet;

import com.fbieck.repository.SymbolRelationRepository;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class TweetReader implements ItemReader<Tweet>, ItemStream {

    @Autowired
    private SymbolRelationRepository symbolRelationRepository;

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
        symbolRelationRepository.findAll().forEach(symbolRelation -> {

            twitter.timelineOperations().getUserTimeline(symbolRelation.getUserid(), 200).forEach(tweet -> {
                tweets.add(tweet);
                tweet.getExtraData().put("custom_userid", symbolRelation.getUserid());
            });
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
