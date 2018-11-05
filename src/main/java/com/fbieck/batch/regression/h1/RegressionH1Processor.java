package com.fbieck.batch.regression.h1;

import com.fbieck.entities.Regression;
import com.fbieck.repository.RegressionRepository;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
@Transactional
@StepScope
public class RegressionH1Processor implements ItemProcessor<OLSMultipleLinearRegression, Regression> {

    private final String hypothesis = "H1";

    @Value("#{jobParameters[hourinterval]}")
    public Integer hourinterval;

    @Autowired
    private RegressionRepository regressionRepository;

    @Override
    public Regression process(OLSMultipleLinearRegression ols) throws Exception {
        Regression regression = regressionRepository.findByHypothesisAndHourinterval(hypothesis, hourinterval);
        if (regression == null) {
            regression = new Regression();
        }
        regression.setHypothesis(hypothesis);
        regression.setHourinterval(hourinterval);
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
