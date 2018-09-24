package com.fbieck.entities.alphavantage;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class TimeSeries {

    public static String FIELD_METADATA = "Meta Data";
    public static String FIELD_INFORMATION = "1. Information";
    public static String FIELD_SYMBOL = "2. Symbol";
    public static String FIELD_LASTREFRESHED = "3. Last Refreshed";
    public static String FIELD_GIVENINTERVAL = "4. Interval";
    public static String FIELD_OUTPUTSIZE = "5. Output Size";
    public static String FIELD_TIMEZONE = "6. Time Zone";

    @Id
    @Column(name = "idtimeseries")
    private String id;

    private String information;

    private String lastRefreshed;

    private String givenInterval;

    private String outputSize;

    private String timezone;

    @ManyToMany
    @JoinTable(name = "timeseries_timeseriesentries", joinColumns = @JoinColumn(name = "idtimeseries"),
            inverseJoinColumns = @JoinColumn(name = "idtimeseriesentry"))
    private List<TimeSeriesEntry> entries;
}
