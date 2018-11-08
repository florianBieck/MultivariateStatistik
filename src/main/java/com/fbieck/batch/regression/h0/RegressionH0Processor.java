package com.fbieck.batch.regression.h0;

import com.fbieck.entities.Regression;
import com.fbieck.entities.RegressionCoefficient;
import com.fbieck.repository.RegressionCoefficientRepository;
import com.fbieck.repository.RegressionRepository;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.util.FastMath;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
@Transactional
@StepScope
public class RegressionH0Processor implements ItemProcessor<OLSMultipleLinearRegression, Regression> {

    private final String hypothesis = "H0";

    @Value("#{jobParameters[hourinterval]}")
    public Integer hourinterval;

    @Autowired
    private RegressionRepository regressionRepository;

    @Autowired
    private RegressionCoefficientRepository regressionCoefficientRepository;

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

        final double[] beta = ols.estimateRegressionParameters();
        final double[] standardErrors = ols.estimateRegressionParametersStandardErrors();
        final int residualdf = ols.estimateResiduals().length - beta.length;

        final TDistribution tdistribution = new TDistribution(residualdf);

        //calculate p-value and create coefficient
        List<RegressionCoefficient> coefficients = new ArrayList<>();
        for (int i = 0; i < beta.length; i++) {
            double tstat = beta[i] / standardErrors[i];
            double pvalue = tdistribution.cumulativeProbability(-FastMath.abs(tstat)) * 2;
            final RegressionCoefficient coefficient = new RegressionCoefficient();
            coefficient.setCoefficient(beta[i]);
            coefficient.setStandardError(standardErrors[i]);
            coefficient.setTStat(tstat);
            coefficient.setPValue(pvalue);

            coefficients.add(coefficient);
        }
        regression.setRegressionCoefficients(coefficients);

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
