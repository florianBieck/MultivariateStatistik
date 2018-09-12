package com.fbieck.service.globalquote;

import com.fbieck.entities.alphavantage.GlobalQuote;

import java.io.IOException;
import java.util.List;

public interface IGlobalQuoteService {

    //List
    Iterable<GlobalQuote> findAll();

    //Single
    GlobalQuote findBySymbol(String symbol) throws IOException;

    //Save All
    Iterable<GlobalQuote> saveAll(List<GlobalQuote> globalQuotes);

    //Delete
    Boolean delete(Integer id);
}
