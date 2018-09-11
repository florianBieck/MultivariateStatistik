package com.fbieck.service.tweet;

import com.fbieck.entities.twitter.Tweet;
import com.fbieck.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService implements ITweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Override
    public Iterable<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet findById(Long id) {
        return tweetRepository.findById(id).orElse(null);
    }

    @Override
    public Tweet create(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet update(Tweet tweet) {
        return tweetRepository.existsById(tweet.getId()) ? tweetRepository.save(tweet) : null;
    }

    @Override
    public Iterable<Tweet> saveAll(List<Tweet> tweets){
        return tweetRepository.saveAll(tweets);
    }

    @Override
    public Boolean delete(Long id) {
        if (tweetRepository.existsById(id)){
            tweetRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
