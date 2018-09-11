package com.fbieck.controller;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
public class DataLoaderController {

    private static Logger logger = LoggerFactory.getLogger(DataLoaderController.class);

    @Value("${custom.featurescount}")
    private int FEATURES_COUNT;

    @Value("${custom.classescount}")
    private int CLASSES_COUNT;

    @RequestMapping(value = "/load")
    private String loader(){

        LocalDateTime start = LocalDateTime.now();
        logger.info("Start loading Data at: "+start);

        //Read CSV Data
        RecordReader recordReader = new CSVRecordReader(0, ',');
        try {
            recordReader.initialize(new FileSplit(
                    new ClassPathResource("iris.data.txt").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataSetIterator iterator = new RecordReaderDataSetIterator(
                recordReader, 150, FEATURES_COUNT, CLASSES_COUNT);
        DataSet allData = iterator.next();
        /*allData.asList().forEach(dataSet -> {
            logger.info(dataSet.get(0).get(0).toString());
        });*/
        logger.info(allData.asList().get(0).get(0).getFeatures().getColumn(0).toString());
        logger.info(allData.asList().get(0).get(0).toString());
        allData.shuffle(42); //To get rid of ordering classes

        LocalDateTime end = LocalDateTime.now();
        logger.info("Finished loading Data at: "+end);

        Duration duration = Duration.between(start, end);
        logger.info("Duration: "+duration.getNano()+"(ns) | "+duration.getSeconds()+"(s)");

        return "Finished Loading Data";
    }
}
