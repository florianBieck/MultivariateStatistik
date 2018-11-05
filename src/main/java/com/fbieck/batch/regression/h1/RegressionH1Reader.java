package com.fbieck.batch.regression.h1;

import com.fbieck.entities.Result;
import com.fbieck.repository.ResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@StepScope
@Slf4j
public class RegressionH1Reader implements ItemReader<OLSMultipleLinearRegression> {

    @Autowired
    private ResultRepository resultRepository;

    @Value("#{jobParameters[hourinterval]}")
    private Integer hourinterval;

    private boolean runned = false;

    @Override
    public OLSMultipleLinearRegression read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (runned) {
            runned = false;
            return null;
        }
        List<Result> results = (List<Result>) resultRepository
                .findAllByChangeIntervalIsNotNullAndRetweetCountIsNotNullAndHourInterval(hourinterval);

        log.info(results.size() + " Resulst geladen.");
        OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();

        List<Double> changeintervals = new ArrayList<>();
        results.forEach(result -> {
            changeintervals.add(result.getChangeInterval());
        });

        double[][] x = new double[results.size()][1];
        for (int i = 0; i < results.size(); i++) {
            x[i][0] = results.get(i).getRetweetCount();
        }
        double[] y = changeintervals.stream().mapToDouble(Double::doubleValue).toArray();

        try {
            olsMultipleLinearRegression.newSampleData(y, x);
        } catch (NoDataException e) {
            //IGNORE
            return null;
        }
        log.info("Finish read");
        runned = true;
        return olsMultipleLinearRegression;
    }
}
