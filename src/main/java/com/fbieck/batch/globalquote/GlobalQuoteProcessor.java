package com.fbieck.batch.globalquote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fbieck.entities.alphavantage.GlobalQuote;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class GlobalQuoteProcessor implements ItemProcessor<JsonNode, GlobalQuote> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public GlobalQuote process(JsonNode jsonNode) throws Exception {
        GlobalQuote globalQuote = new GlobalQuote();
        globalQuote.setSymbol(jsonNode.path("01. symbol").textValue());
        globalQuote.setOpen(jsonNode.path("02. open").asDouble());
        globalQuote.setHigh(jsonNode.path("03. high").asDouble());
        globalQuote.setLow(jsonNode.path("04. low").asDouble());
        globalQuote.setPrice(jsonNode.path("05. price").asDouble());
        globalQuote.setVolume(jsonNode.path("06. volume").asDouble());
        globalQuote.setLatestTradingDay(LocalDate.parse(jsonNode.path("07. latest trading day").textValue(), dateTimeFormatter));
        globalQuote.setPreviousClose(jsonNode.path("08. previous close").asDouble());
        globalQuote.setChangeAbsolute(jsonNode.path("09. change").asDouble());
        globalQuote.setChangePercent(Double.parseDouble(jsonNode.path("10. change percent").textValue().replace("%", "")));
        return globalQuote;
    }
}
