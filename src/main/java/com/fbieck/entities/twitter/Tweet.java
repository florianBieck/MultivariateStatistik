package com.fbieck.entities.twitter;

import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Tweet {

    @Id
    @Column(name = "idtweet")
    private Long id;

    private String text;

    private String creator;

    private LocalDateTime createdAt;

    private Integer favouriteCount;

    private Integer retweetCount;

    private String byHashtag;
}
