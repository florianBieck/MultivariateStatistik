package com.fbieck.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IrisDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idsingledata")
    private Integer id;

    private Double sepalLength;

    private Double sepalWidth;

    private Double petalLength;

    private Double petalWidth;

    private Integer classindex;
}
