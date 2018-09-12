package com.fbieck.batch.timeseries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TimeSeriesProcessor implements ItemProcessor<JsonNode, TimeSeries> {
    @Override
    public TimeSeries process(JsonNode jsonNode) throws Exception {
        TimeSeries timeSeries = new TimeSeries();

        //Metadata
        timeSeries.setInformation(jsonNode.get("Meta Data").path("1. Information").textValue());
        timeSeries.setSymbol(jsonNode.get("Meta Data").path("2. Symbol").textValue());
        timeSeries.setLastRefreshed(jsonNode.get("Meta Data").path("3. Last Refreshed").textValue());
        timeSeries.setInterval(jsonNode.get("Meta Data").path("4. Interval").textValue());
        timeSeries.setOutputsize(jsonNode.get("Meta Data").path("5. Output Size").textValue());
        timeSeries.setTimezone(jsonNode.get("Meta Data").path("6. Time Zone").textValue());

        //Entries
        timeSeries.setEntries(new ArrayList<>());
        jsonNode.get("Time Series (5min)").elements().forEachRemaining(element -> {
            TimeSeriesEntry timeSeriesEntry = new TimeSeriesEntry();
            timeSeriesEntry.setOpen(element.path("1. open").asDouble());
            timeSeriesEntry.setHigh(element.path("2. high").asDouble());
            timeSeriesEntry.setLow(element.path("3. low").asDouble());
            timeSeriesEntry.setClose(element.path("4. close").asDouble());
            timeSeriesEntry.setVolume(element.path("5. volume").asDouble());
            timeSeries.getEntries().add(timeSeriesEntry);
        });
        return timeSeries;
    }
}
