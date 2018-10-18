package com.fbieck.batch.stockchange;

import com.fbieck.entities.StockChange;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.StockChangeRepository;
import com.fbieck.repository.SymbolRelationRepository;
import com.fbieck.repository.TimeSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
@StepScope
@Slf4j
public class StockChangeProcessor implements ItemProcessor<Tweet, StockChange> {

    @Autowired
    private SymbolRelationRepository symbolRelationRepository;
    @Autowired
    private TimeSeriesRepository timeSeriesRepository;

    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Value("#{jobParameters[hourinterval]}")
    private Integer hourinterval;

    @Override
    public StockChange process(Tweet tweet) throws Exception {

        TimeSeries timeSeries = timeSeriesRepository.findById(
                symbolRelationRepository.findByUserid(tweet.getUserid()).getSymbol()
        );

        if (timeSeries != null && timeSeries.getEntries().size() > 0) {

            TreeSet<LocalDateTime> entryTreeSet = timeSeries.getEntries()
                    .stream().map(TimeSeriesEntry::getTimestamp).collect(Collectors.toCollection(TreeSet::new));
            LocalDateTime startLDT;
            if (tweet.getCreatedAt().compareTo(LocalDateTime.now()) > 0) {
                startLDT = entryTreeSet.first();
            } else {
                startLDT = entryTreeSet.floor(tweet.getCreatedAt());
                startLDT = startLDT == null ? entryTreeSet.last() : startLDT;
            }
            LocalDateTime endLDT;
            if (tweet.getCreatedAt().plusHours(hourinterval).compareTo(LocalDateTime.now()) > 0) {
                endLDT = entryTreeSet.first();
            } else {
                endLDT = entryTreeSet.floor(tweet.getCreatedAt().plusHours(hourinterval));
                endLDT = endLDT == null ? entryTreeSet.last() : endLDT;
            }

            LocalDateTime finalStartLDT = startLDT;
            LocalDateTime finalEndLDT = endLDT;

            TimeSeriesEntry timeSeriesEntryAt = timeSeries.getEntries()
                    .stream().filter(timeSeriesEntry -> timeSeriesEntry.getTimestamp().equals(finalStartLDT))
                    .findFirst().orElse(null);
            TimeSeriesEntry timeSeriesEntryInterval = timeSeries.getEntries()
                    .stream().filter(timeSeriesEntry -> timeSeriesEntry.getTimestamp().equals(finalEndLDT))
                    .findFirst().orElse(null);

            StockChange stockChange = stockChangeRepository.findByTweetAndHourInterval(tweet, hourinterval);

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
                return stockChange;
            } else {
                log.info("Start: " + startLDT + "\tEnd:" + endLDT + "\tstartTSE:" + timeSeriesEntryAt + "\tendTSE:" + timeSeriesEntryInterval);
            }
        }
        return null;
    }
}
