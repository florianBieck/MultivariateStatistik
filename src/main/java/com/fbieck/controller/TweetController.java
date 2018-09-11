package com.fbieck.controller;

import com.fbieck.service.tweet.ITweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tweet")
public class TweetController {

    @Autowired
    private ITweetService tweetService;

    /*@GetMapping(value = "/load")
    private ResponseEntity<Iterable<Tweet>> findAll(){
        return ResponseEntity.ok(tweetService.saveAll(tweetLoader.getTweets()));
    }*/
}
