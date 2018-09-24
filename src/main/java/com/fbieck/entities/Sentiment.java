package com.fbieck.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Sentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsentiment")
    private Integer id;

    private Integer countWords;

    private Integer countPositives;

    private Integer countNegatives;

    private Double positivity;
}
