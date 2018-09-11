package com.fbieck.repository;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Long> {
}
