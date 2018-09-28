package com.fbieck.batch.stockchange;

import com.fbieck.entities.StockChange;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.StockChangeRepository;
import com.fbieck.repository.SymbolRelationRepository;
import com.fbieck.repository.TimeSeriesRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class StockChangeProcessor implements ItemProcessor<Tweet, StockChange> {

    @Autowired
    private SymbolRelationRepository symbolRelationRepository;
    @Autowired
    private TimeSeriesRepository timeSeriesRepository;

    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Override
    public StockChange process(Tweet tweet) throws Exception {

        TimeSeries timeSeries = timeSeriesRepository.findById(
                symbolRelationRepository.findByUserid(tweet.getUserid()).getSymbol()
        );

        int hourinterval = 24;

        TimeSeriesEntry timeSeriesEntryAt = timeSeries.getEntries().stream()
                .max(Comparator.comparing(x -> x.getTimestamp()
                        .compareTo(tweet.getCreatedAt())))
                .orElse(null);

        TimeSeriesEntry timeSeriesEntryInterval = timeSeries.getEntries().stream()
                .max(Comparator.comparing(x -> x.getTimestamp()
                        .compareTo(tweet.getCreatedAt().plusHours(hourinterval))))
                .orElse(null);

        StockChange stockChange = stockChangeRepository.findByTweet(tweet);

        if (timeSeriesEntryAt != null && timeSeriesEntryInterval != null) {
            if (stockChange == null) {
                stockChange = new StockChange();
            }
            stockChange.setDateTimeStart(timeSeriesEntryAt.getTimestamp());
            stockChange.setDateTimeEnd(timeSeriesEntryInterval.getTimestamp());
            stockChange.setHourInterval(hourinterval);
            stockChange.setPriceStart(timeSeriesEntryAt.getClose());
            stockChange.setPriceEnd(timeSeriesEntryInterval.getClose());
            stockChange.setTweet(tweet);
            tweet.setStockChange(stockChange);
        }
        return stockChange;
    }
}
