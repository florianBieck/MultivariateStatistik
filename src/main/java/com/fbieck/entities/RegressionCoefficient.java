package com.fbieck.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RegressionCoefficient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idregressioncoefficient")
    private Integer id;

    private Double coefficient;

    private Double standardError;

    private Double tStat;

    private Double pValue;
}
