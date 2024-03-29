package com.fbieck.repository;

import com.fbieck.entities.StockChange;
import com.fbieck.entities.twitter.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockChangeRepository extends CrudRepository<StockChange, Integer> {

    StockChange findByTweetAndHourInterval(Tweet tweet, Integer hourinterval);
}
