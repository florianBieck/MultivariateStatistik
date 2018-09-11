package com.fbieck.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private static final int CLASSES_COUNT = 3;
    private static final int FEATURES_COUNT = 4;

    private static Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent){

        /*LocalDateTime start = LocalDateTime.now();
        logger.info("Start: "+start.toString());

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
        allData.shuffle(42); //To get rid of ordering classes

        //Normalizing data
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(allData);
        normalizer.transform(allData);

        //Spliting data for training and testing purposes
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);
        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        //Network configuration
        MultiLayerConfiguration configuration
                = new NeuralNetConfiguration.Builder()
                .iterations(1000)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .learningRate(0.1)
                .regularization(true).l2(0.0001)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(3).build())
                .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
                .layer(2, new OutputLayer.Builder(
                        LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(3).nOut(CLASSES_COUNT).build())
                .backprop(true).pretrain(false)
                .build();

        //Network
        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();
        model.fit(trainingData);

        //Testing the network
        INDArray output = model.output(testData.getFeatureMatrix());
        Evaluation eval = new Evaluation(3);
        eval.eval(testData.getLabels(), output);

        LocalDateTime end = LocalDateTime.now();

        //Output
        logger.info(eval.stats());
        logger.info("Started: "+start);
        logger.info("Finished: "+end);
        Duration duration = Duration.between(start, end);
        logger.info("Duration: "+duration.getNano()+"(ns) | "+duration.getSeconds()+"(s)");*/
    }
}
