package com.fbieck.batch.regression.h0;

import com.fbieck.entities.Result;
import com.fbieck.repository.ResultRepository;
import com.google.common.collect.Lists;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class RegressionH0Reader implements ItemReader<OLSMultipleLinearRegression>, ItemStream {

    private static Logger logger = LoggerFactory.getLogger(RegressionH0Reader.class);

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public OLSMultipleLinearRegression read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<Result> results = Lists.newArrayList(resultRepository.findAllByChangeIntervalIsNotNullAndPositivityIsNotNull())
                .stream().filter(result -> result.getChangeInterval() != 0.0)
                .collect(Collectors.toList());

        OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();

        List<Double> changeintervals = new ArrayList<>();
        results.forEach(result -> {
            changeintervals.add(result.getChangeInterval());
        });

        double[][] x = new double[results.size()][1];
        for (int i = 0; i < results.size(); i++) {
            x[i][0] = results.get(i).getPositivity();
        }
        double[] y = changeintervals.stream().mapToDouble(Double::doubleValue).toArray();

        try {
            olsMultipleLinearRegression.newSampleData(y, x);
        } catch (NoDataException e) {
            //IGNORE
            return null;
        }
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
