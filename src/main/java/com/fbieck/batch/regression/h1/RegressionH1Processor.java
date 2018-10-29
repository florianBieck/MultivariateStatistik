package com.fbieck.batch.regression.h1;

import com.fbieck.entities.Regression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
@Transactional
public class RegressionH1Processor implements ItemProcessor<OLSMultipleLinearRegression, Regression> {

    private final Integer id = 1;

    @Override
    public Regression process(OLSMultipleLinearRegression ols) throws Exception {
        Regression regression = new Regression();
        regression.setId(id);
        regression.setEstimatedParameters(
                DoubleStream.of(ols.estimateRegressionParameters())
                        .boxed().collect(Collectors.toList())
        );
        regression.setEstimatedResiduals(
                DoubleStream.of(ols.estimateResiduals())
                        .boxed().collect(Collectors.toList())
        );
        regression.setEstimatedParametersStandardErrors(
                DoubleStream.of(ols.estimateRegressionParametersStandardErrors())
                        .boxed().collect(Collectors.toList())
        );
        regression.setEstimatedErrorVariance(ols.estimateErrorVariance());
        regression.setEstimatedRegressAndVariance(ols.estimateRegressandVariance());
        regression.setEstimatedRegressionStandardError(ols.estimateRegressionStandardError());
        regression.setCalculatedAdjustedRSquared(ols.calculateAdjustedRSquared());
        regression.setCalculatedResidualSumOfSquares(ols.calculateResidualSumOfSquares());
        regression.setCalculatedRSquared(ols.calculateRSquared());
        regression.setCalculatedTotalSumOfSquares(ols.calculateTotalSumOfSquares());
        return regression;
    }
}
