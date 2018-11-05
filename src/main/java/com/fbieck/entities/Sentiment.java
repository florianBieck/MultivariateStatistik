package com.fbieck.entities;

import com.fbieck.entities.twitter.Tweet;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Sentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idsentiment")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "idtweet")
    private Tweet tweet;

    private Integer countWords;

    private Integer countPositives;

    private Integer countNegatives;

    private Double positivity;
}
