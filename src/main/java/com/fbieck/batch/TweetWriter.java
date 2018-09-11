package com.fbieck.batch;

import com.fbieck.entities.twitter.Tweet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TweetWriter implements ItemWriter<Tweet> {
    @Override
    public void write(List<? extends Tweet> list) throws Exception {
        list.forEach(element -> System.out.println(element));
    }
}
