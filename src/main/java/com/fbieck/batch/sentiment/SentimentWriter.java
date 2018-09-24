package com.fbieck.batch.sentiment;

import com.fbieck.entities.Sentiment;
import com.fbieck.repository.SentimentRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SentimentWriter implements ItemWriter<Sentiment> {

    @Autowired
    private SentimentRepository sentimentRepository;

    @Override
    public void write(List<? extends Sentiment> list) throws Exception {
        sentimentRepository.saveAll((List<Sentiment>) list);
    }
}
