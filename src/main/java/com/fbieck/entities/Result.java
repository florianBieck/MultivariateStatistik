package com.fbieck.entities;

import com.fbieck.entities.twitter.Tweet;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresult")
    private Integer id;

    private Double positivity;

    private Double changeInterval;

    private Integer hourInterval;

    private Integer favouriteCount;

    private Integer retweetCount;

    @ManyToOne
    @JoinColumn(name = "idtweet")
    private Tweet tweet;
}
