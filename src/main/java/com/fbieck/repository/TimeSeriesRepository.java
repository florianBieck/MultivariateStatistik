package com.fbieck.repository;

import com.fbieck.entities.alphavantage.TimeSeries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSeriesRepository extends CrudRepository<TimeSeries, Integer> {

    TimeSeries findById(String id);
}
