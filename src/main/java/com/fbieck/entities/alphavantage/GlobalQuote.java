package com.fbieck.entities.alphavantage;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class GlobalQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idglobalquote")
    private Integer id;

    private String symbol;

    private Double open;

    private Double high;

    private Double low;

    private Double price;

    private Double volume;

    private LocalDate latestTradingDay;

    private Double previousClose;

    private Double change;

    private Double changePercent;
}
