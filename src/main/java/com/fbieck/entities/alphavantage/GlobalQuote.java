package com.fbieck.entities.alphavantage;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class GlobalQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idglobalquote")
    private Integer id;

    @Column(length = 60)
    private String symbol;

    @Column(precision = 1)
    private Double open;

    @Column(precision = 1)
    private Double high;

    @Column(precision = 1)
    private Double low;

    @Column(precision = 1)
    private Double price;

    @Column(precision = 1)
    private Double volume;

    private LocalDate latestTradingDay;

    @Column(precision = 1)
    private Double previousClose;

    @Column(precision = 1)
    private Double changeAbsolute;

    @Column(precision = 1)
    private Double changePercent;
}
