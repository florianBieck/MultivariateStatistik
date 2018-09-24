package com.fbieck.repository;

import com.fbieck.entities.Sentiment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentimentRepository extends CrudRepository<Sentiment, Integer> {
}
