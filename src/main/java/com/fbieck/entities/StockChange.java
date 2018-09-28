package com.fbieck.entities;

import com.fbieck.entities.twitter.Tweet;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Data
@Entity
public class StockChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstockchange")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "idtweet")
    private Tweet tweet;

    private Integer hourInterval;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime dateTimeStart;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime dateTimeEnd;

    private Double priceStart;

    private Double priceEnd;
}
