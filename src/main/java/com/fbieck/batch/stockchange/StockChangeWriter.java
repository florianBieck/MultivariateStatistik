package com.fbieck.batch.stockchange;

import com.fbieck.entities.StockChange;
import com.fbieck.repository.StockChangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@Slf4j
public class StockChangeWriter implements ItemWriter<StockChange> {

    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Override
    public void write(List<? extends StockChange> list) throws Exception {
        List<StockChange> lst = list.stream()
                .filter(stockchange -> ((StockChange) stockchange).getDateTimeStart()
                        .compareTo(((StockChange) stockchange).getDateTimeEnd()) != 0)
                .collect(Collectors.toList());
        log.info("Filter: " + list.size() + " - " + lst.size() + " = " + (list.size() - lst.size()));
        stockChangeRepository.saveAll(lst);
    }
}
