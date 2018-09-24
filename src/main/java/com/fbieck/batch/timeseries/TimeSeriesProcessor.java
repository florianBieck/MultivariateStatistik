package com.fbieck.batch.timeseries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.entities.alphavantage.TimeSeries;
import com.fbieck.entities.alphavantage.TimeSeriesEntry;
import com.google.common.collect.Lists;
import org.joda.time.LocalDateTime;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TimeSeriesProcessor implements ItemProcessor<JsonNode, TimeSeries> {
    @Override
    public TimeSeries process(JsonNode jsonNode) throws Exception {
        TimeSeries timeSeries = new TimeSeries();

        //Metadata
        timeSeries.setInformation(jsonNode.get(TimeSeries.FIELD_METADATA)
                .path(TimeSeries.FIELD_INFORMATION).textValue());
        timeSeries.setId(jsonNode.get(TimeSeries.FIELD_METADATA)
                .path(TimeSeries.FIELD_SYMBOL).textValue());
        timeSeries.setLastRefreshed(jsonNode.get(TimeSeries.FIELD_METADATA)
                .path(TimeSeries.FIELD_LASTREFRESHED).textValue());
        timeSeries.setGivenInterval(jsonNode.get(TimeSeries.FIELD_METADATA)
                .path(TimeSeries.FIELD_GIVENINTERVAL).textValue());
        timeSeries.setOutputSize(jsonNode.get(TimeSeries.FIELD_METADATA)
                .path(TimeSeries.FIELD_OUTPUTSIZE).textValue());
        timeSeries.setTimezone(jsonNode.get(TimeSeries.FIELD_METADATA).path(TimeSeries.FIELD_TIMEZONE).textValue());

        //Entries
        timeSeries.setEntries(new ArrayList<>());
        List<JsonNode> elements = Lists.newArrayList(jsonNode.get(TimeSeriesEntry.FIELD_NAME).elements());
        ArrayList<Map.Entry<String, JsonNode>> fields =
                Lists.newArrayList(jsonNode.get(TimeSeriesEntry.FIELD_NAME).fields());
        for (int i = 0; i < elements.size(); i++) {
            JsonNode element = elements.get(i);
            TimeSeriesEntry timeSeriesEntry = new TimeSeriesEntry();
            timeSeriesEntry.setTimestamp(
                    LocalDateTime.parse(fields.get(i).getKey().replace(" ", "T")));
            timeSeriesEntry.setOpen(element.path(TimeSeriesEntry.FIELD_OPEN).asDouble());
            timeSeriesEntry.setHigh(element.path(TimeSeriesEntry.FIELD_HIGH).asDouble());
            timeSeriesEntry.setLow(element.path(TimeSeriesEntry.FIELD_LOW).asDouble());
            timeSeriesEntry.setClose(element.path(TimeSeriesEntry.FIELD_CLOSE).asDouble());
            timeSeriesEntry.setVolume(element.path(TimeSeriesEntry.FIELD_VOLUME).asDouble());
            timeSeries.getEntries().add(timeSeriesEntry);
        }
        return timeSeries;
    }
}
