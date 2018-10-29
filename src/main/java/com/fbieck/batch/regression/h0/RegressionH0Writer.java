package com.fbieck.batch.regression.h0;

import com.fbieck.entities.Regression;
import com.fbieck.repository.RegressionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class RegressionH0Writer implements ItemWriter<Regression> {

    @Autowired
    private RegressionRepository regressionRepository;

    @Override
    public void write(List<? extends Regression> list) throws Exception {
        //list.forEach(regression -> System.out.println(regression));
        regressionRepository.saveAll(list);
    }
}
