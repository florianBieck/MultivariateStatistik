package com.fbieck.batch.tweet;

import com.fbieck.entities.twitter.Tweet;
import com.fbieck.service.tweet.ITweetService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TweetWriter implements ItemWriter<Tweet> {

    @Autowired
    private ITweetService tweetService;

    @Override
    public void write(List<? extends Tweet> list) throws Exception {
        //list.forEach(element -> System.out.println(element.getId()+" "+element.getPositivegrade()+" "+element.getNegativegrade()));
        tweetService.saveAll((List<Tweet>) list);
    }
}
