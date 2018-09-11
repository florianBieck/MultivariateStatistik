package com.fbieck.repository;

import com.fbieck.entities.alphavantage.GlobalQuote;
import org.springframework.data.repository.CrudRepository;

public interface GlobalQuoteRepository extends CrudRepository<GlobalQuote, Integer> {
}
