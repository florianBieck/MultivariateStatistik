package com.fbieck.batch.result;

import com.fbieck.entities.Result;
import com.fbieck.repository.ResultRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultWriter implements ItemWriter<Result> {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public void write(List<? extends Result> list) throws Exception {
        resultRepository.saveAll(list);
    }
}
