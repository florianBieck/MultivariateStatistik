package com.fbieck.service.globalquote;

import com.fbieck.entities.alphavantage.GlobalQuote;

import java.io.IOException;

public interface IGlobalQuoteService {

    //Single
    GlobalQuote findBySymbol(String symbol) throws IOException;
}
