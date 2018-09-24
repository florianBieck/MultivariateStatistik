package com.fbieck.batch.sentiment;

import com.fbieck.entities.Sentiment;
import com.fbieck.entities.twitter.Tweet;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
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
public class SentimentProcessor implements ItemProcessor<Tweet, Sentiment> {


    @Nullable
    public static Integer getCount(List<String> words, String list) {
        try {
            IntStream.Builder intStreamBuilder = IntStream.builder();
            Resource resource = new ClassPathResource(list);
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
        return null;
    }

    @Override
    public Sentiment process(Tweet tweet) throws Exception {
        List<String> words = preprocessIntoWordList(tweet.getText());

        Sentiment sentiment = new Sentiment();
        sentiment.setCountWords(words.size());
        sentiment.setCountPositives(getPositiveCount(words));
        sentiment.setCountNegatives(getNegativeCount(words));
        sentiment.setPositivity(calculatePositivity(sentiment));
        return sentiment;
    }

    private List<String> preprocessIntoWordList(String text) {
        return Lists
                .newArrayList(text.split(" "))
                .stream()
                .filter(s -> !(s.contains("http://") || s.contains("https://")))
                /*"")
                        .replaceAll("&", "")
                        .replaceAll("-", "")
                        .replaceAll(":", "")
                        .replaceAll("@", ""))*/
                .collect(Collectors.toList());
    }

    private Double calculatePositivity(Sentiment sentiment) {
        return (double) ((sentiment.getCountPositives() / sentiment.getCountWords()) -
                (sentiment.getCountNegatives() / sentiment.getCountWords()));
    }

    private Integer getPositiveCount(List<String> words) {
        Integer intStreamBuilder = getCount(words, "positive-words.txt");
        if (intStreamBuilder != null) return intStreamBuilder;
        return 0;
    }

    private Integer getNegativeCount(List<String> words) {
        Integer intStreamBuilder = getCount(words, "negative-words.txt");
        if (intStreamBuilder != null) return intStreamBuilder;
        return 0;
    }
}
