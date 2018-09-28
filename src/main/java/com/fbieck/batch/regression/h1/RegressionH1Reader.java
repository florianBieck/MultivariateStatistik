package com.fbieck.batch.regression.h1;

import com.fbieck.entities.Result;
import com.fbieck.repository.ResultRepository;
import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegressionH1Reader implements ItemReader<OLSMultipleLinearRegression>, ItemStream {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public OLSMultipleLinearRegression read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<Result> results = Lists.newArrayList(resultRepository.findAll());

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

        olsMultipleLinearRegression.newSampleData(y, x);
        return olsMultipleLinearRegression;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
