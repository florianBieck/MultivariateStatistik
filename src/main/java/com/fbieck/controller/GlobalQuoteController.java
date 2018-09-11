package com.fbieck.controller;

import com.fbieck.entities.alphavantage.GlobalQuote;
import com.fbieck.service.globalquote.IGlobalQuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(value = "/globalquote")
public class GlobalQuoteController {

    @Autowired
    private IGlobalQuoteService globalQuoteService;

    private static Logger logger = LoggerFactory.getLogger(GlobalQuoteController.class);

    @GetMapping(value = "/{symbol}")
    private ResponseEntity<GlobalQuote> findByName(@PathVariable String symbol) throws IOException {
        return ResponseEntity.ok(globalQuoteService.findBySymbol(symbol));
    }
}
