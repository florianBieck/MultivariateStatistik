package com.fbieck.repository;

import com.fbieck.entities.Result;
import com.fbieck.entities.twitter.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {

    Result findByTweetAndHourInterval(Tweet tweet, Integer hourinterval);
}
