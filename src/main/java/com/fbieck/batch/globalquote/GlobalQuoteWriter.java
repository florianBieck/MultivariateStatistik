package com.fbieck.batch.globalquote;

import com.fbieck.entities.alphavantage.GlobalQuote;
import com.fbieck.service.globalquote.IGlobalQuoteService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalQuoteWriter implements ItemWriter<GlobalQuote> {

    @Autowired
    private IGlobalQuoteService globalQuoteService;

    @Override
    public void write(List<? extends GlobalQuote> list) throws Exception {
        //list.forEach(element -> System.out.println(element));
        globalQuoteService.saveAll((List<GlobalQuote>) list);
    }
}
