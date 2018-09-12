package com.fbieck.service.globalquote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbieck.entities.alphavantage.GlobalQuote;
import com.fbieck.repository.GlobalQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GlobalQuoteService implements IGlobalQuoteService{

    @Autowired
    private GlobalQuoteRepository globalQuoteRepository;

    @Value("${custom.alphavantage.apikey}")
    private String APIKEY;

    @Override
    public Iterable<GlobalQuote> findAll() {
        return globalQuoteRepository.findAll();
    }

    @Override
    public GlobalQuote findBySymbol(String symbol) throws IOException {
        final String uri = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol
                +"&apikey="+APIKEY;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody()).get("Global Quote");

        GlobalQuote globalQuote = new GlobalQuote();
        globalQuote.setSymbol(root.path("01. symbol").textValue());
        globalQuote.setOpen(root.path("02. open").asDouble());
        globalQuote.setHigh(root.path("03. high").asDouble());
        globalQuote.setLow(root.path("04. low").asDouble());
        globalQuote.setPrice(root.path("05. price").asDouble());
        globalQuote.setVolume(root.path("06. volume").asDouble());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        globalQuote.setLatestTradingDay(LocalDate.parse(root.path("07. latest trading day").textValue(), dateTimeFormatter));
        globalQuote.setPreviousClose(root.path("08. previous close").asDouble());
        globalQuote.setChangeAbsolute(root.path("09. changeAbsolute").asDouble());
        globalQuote.setChangePercent(Double.parseDouble(root.path("10. changeAbsolute percent").textValue().replace("%", "")));

        return globalQuote;
    }

    @Override
    public Iterable<GlobalQuote> saveAll(List<GlobalQuote> globalQuotes) {
        return globalQuoteRepository.saveAll(globalQuotes);
    }

    @Override
    public Boolean delete(Integer id) {
        if (globalQuoteRepository.existsById(id)) {
            globalQuoteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
