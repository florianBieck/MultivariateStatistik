package com.fbieck.entities.twitter;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Tweet {

    @Id
    @Column(name = "idtweet")
    private Long id;

    private String text;

    private String creator;

    private double positivegrade;

    private double negativegrade;

    private Date createdAt;

    private Integer favouriteCount;

    private Integer retweetCount;
}
