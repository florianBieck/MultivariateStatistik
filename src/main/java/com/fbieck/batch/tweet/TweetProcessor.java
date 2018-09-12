package com.fbieck.batch.tweet;

import com.fbieck.entities.twitter.Tweet;
import com.google.common.collect.Lists;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
public class TweetProcessor implements ItemProcessor<Tweet, Tweet> {


    @Override
    public Tweet process(Tweet tweet) throws Exception {
        List<String> words = Lists
                .newArrayList(tweet.getText().split(" "))
                .stream()
                .filter(s -> !(s.contains("http://") || s.contains("https://")))
                /*"")
                        .replaceAll("&", "")
                        .replaceAll("-", "")
                        .replaceAll(":", "")
                        .replaceAll("@", ""))*/
                .collect(Collectors.toList());
        double positive = getPositiveCount(words);
        double negative = getNegativeCount(words);
        double max = words.size();
        tweet.setPositivegrade(positive / max);
        tweet.setNegativegrade(negative / max);
        if (positive == 0 && negative == 0) {
            return null;
        }
        return tweet;
    }

    private Integer getPositiveCount(List<String> words) {
        try {
            IntStream.Builder intStreamBuilder = IntStream.builder();
            Resource resource = new ClassPathResource("positive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            List<String> positives = reader.lines().collect(Collectors.toList());
            words.forEach(s -> {
                if (positives.contains(s)) {
                    intStreamBuilder.accept(1);
                }
            });
            return intStreamBuilder.build().sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Integer getNegativeCount(List<String> words) {
        try {
            IntStream.Builder intStreamBuilder = IntStream.builder();
            Resource resource = new ClassPathResource("negative-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            List<String> negatives = reader.lines().collect(Collectors.toList());
            words.forEach(s -> {
                if (negatives.contains(s)) {
                    intStreamBuilder.accept(1);
                }
            });
            return intStreamBuilder.build().sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
