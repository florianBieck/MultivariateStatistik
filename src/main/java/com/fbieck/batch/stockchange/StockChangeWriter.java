package com.fbieck.batch.stockchange;

import com.fbieck.entities.StockChange;
import com.fbieck.repository.StockChangeRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockChangeWriter implements ItemWriter<StockChange> {

    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Override
    public void write(List<? extends StockChange> list) throws Exception {
        stockChangeRepository.saveAll(list);
    }
}
