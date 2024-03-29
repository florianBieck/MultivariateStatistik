package com.fbieck.repository;

import com.fbieck.entities.Sentiment;
import com.fbieck.entities.twitter.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentimentRepository extends CrudRepository<Sentiment, Integer> {

    Sentiment findByTweet(Tweet tweet);
}
