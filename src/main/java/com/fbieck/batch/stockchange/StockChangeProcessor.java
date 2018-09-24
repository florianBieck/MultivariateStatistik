package com.fbieck.batch.stockchange;

import com.fbieck.entities.StockChange;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.SymbolRelationRepository;
import com.fbieck.repository.TimeSeriesRepository;
import org.joda.time.DateTimeComparator;
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

    @Override
    public StockChange process(Tweet tweet) throws Exception {

        TimeSeries timeSeries = timeSeriesRepository.findById(
                symbolRelationRepository.findByHashtag(tweet.getByHashtag()).getSymbol()
        );

        TimeSeriesEntry timeSeriesEntryAt = timeSeries.getEntries().stream()
                .min(Comparator.comparingInt(x ->
                        DateTimeComparator.getInstance().compare(x.getTimestamp(), tweet.getCreatedAt())))
                .orElse(null);

        TimeSeriesEntry timeSeriesEntryInterval = timeSeries.getEntries().stream()
                .min(Comparator.comparingInt(x ->
                        DateTimeComparator.getInstance().compare(x.getTimestamp(),
                                tweet.getCreatedAt().plusHours(24))))
                .orElse(null);

        StockChange stockChange = null;

        if (timeSeriesEntryAt != null && timeSeriesEntryInterval != null) {
            stockChange = new StockChange();
            stockChange.setDateTimeStart(timeSeriesEntryAt.getTimestamp());
            stockChange.setDateTimeEnd(timeSeriesEntryInterval.getTimestamp());
            stockChange.setHourInterval(24);
            stockChange.setPriceStart(timeSeriesEntryAt.getClose());
            stockChange.setPriceEnd(timeSeriesEntryInterval.getClose());
        }
        return stockChange;
    }
}
