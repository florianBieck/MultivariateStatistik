package com.fbieck.entities.alphavantage;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Data
@Entity
public class TimeSeriesEntry {

    public static String FIELD_NAME = "Time Series (5min)";
    public static String FIELD_OPEN = "1. open";
    public static String FIELD_HIGH = "2. high";
    public static String FIELD_LOW = "3. low";
    public static String FIELD_CLOSE = "4. close";
    public static String FIELD_VOLUME = "5. volume";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtimeseriesentry")
    private Integer id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime timestamp;

    private Double open;

    private Double high;

    private Double low;

    private Double close;

    private Double volume;
}
