package com.fbieck.batch.timeseries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TimeSeriesReader implements ItemReader<JsonNode>, ItemStream {

    private final List<String> symbols = Arrays.asList("MSFT");

    @Value("${custom.alphavantage.apikey}")
    private String APIKEY;

    private List<JsonNode> nodes;

    @Override
    public JsonNode read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!nodes.isEmpty()) {
            return nodes.remove(0);
        }
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        nodes = new ArrayList<>();
        symbols.forEach(symbol -> {
            String uri = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol
                    + "&interval=5min&apikey=" + APIKEY;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            ObjectMapper mapper = new ObjectMapper();
            try {
                nodes.add(mapper.readTree(response.getBody()));
                mapper.writeValue(new FileOutputStream(symbol + ".rest"),
                        mapper.readTree(response.getBody()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        nodes.clear();
    }
}
