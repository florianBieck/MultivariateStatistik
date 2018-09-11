package com.fbieck.service.tweet;


import com.fbieck.entities.twitter.Tweet;

import java.util.List;

public interface ITweetService {

    //List
    Iterable<Tweet> findAll();

    //Single
    Tweet findById(Long id);

    //Create
    Tweet create(Tweet tweet);

    //Update
    Tweet update(Tweet tweet);

    //Save All
    Iterable<Tweet> saveAll(List<Tweet> tweets);

    //Delete
    Boolean delete(Long id);
}
