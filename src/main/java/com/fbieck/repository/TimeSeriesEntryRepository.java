package com.fbieck.repository;

import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSeriesEntryRepository extends CrudRepository<TimeSeriesEntry, Integer> {
}
