package com.fbieck.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Regression {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idregression")
    private Integer id;

    private String hypothesis;

    private Integer hourinterval;

    @ElementCollection
    @OrderColumn
    private List<Double> estimatedResiduals;

    @ElementCollection
    @OrderColumn
    private List<Double> estimatedParameters;

    @ElementCollection
    @OrderColumn
    private List<Double> estimatedParametersStandardErrors;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "regression_regressioncoefficients", joinColumns = @JoinColumn(name = "idregression"),
            inverseJoinColumns = @JoinColumn(name = "idregressioncoefficient"))
    private List<RegressionCoefficient> regressionCoefficients;

    private Double estimatedErrorVariance;

    private Double estimatedRegressionStandardError;

    private Double estimatedRegressAndVariance;

    private Double calculatedAdjustedRSquared;

    private Double calculatedRSquared;

    private Double calculatedTotalSumOfSquares;

    private Double calculatedResidualSumOfSquares;
}
