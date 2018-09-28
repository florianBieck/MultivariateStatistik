package com.fbieck.batch.timeseries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbieck.repository.SymbolRelationRepository;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeSeriesReader implements ItemReader<JsonNode>, ItemStream {

    @Autowired
    private SymbolRelationRepository symbolRelationRepository;

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
        symbolRelationRepository.findAll().forEach(symbol -> {
            String uri = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol.getSymbol()
                    + "&interval=5min&outputsize=full&apikey=" + APIKEY;
            if (new File(URI.create("file:///" + symbol.getSymbol() + ".json")).exists()) {
                uri = "file:///" + symbol.getSymbol() + ".json";
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            ObjectMapper mapper = new ObjectMapper();
            try {
                nodes.add(mapper.readTree(response.getBody()));
                mapper.writeValue(new FileOutputStream(symbol.getSymbol() + ".json"),
                        response.getBody());
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
