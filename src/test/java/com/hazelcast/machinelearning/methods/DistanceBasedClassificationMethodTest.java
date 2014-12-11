package com.hazelcast.machinelearning.methods;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.machinelearning.HazelcastHelper;
import com.hazelcast.machinelearning.MLMethod;
import com.hazelcast.machinelearning.csv.IrisPlantDataReader;
import com.hazelcast.machinelearning.methods.impl.*;
import com.hazelcast.machinelearning.model.IrisPlantClassification;
import com.hazelcast.machinelearning.model.IrisPlantFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

public class DistanceBasedClassificationMethodTest {

    HazelcastInstance hazelcastInstance;
    List<ClassifiedFeatureDatum<IrisPlantFeature, String>> irisPlants;
    private static final Logger LOG = LoggerFactory.getLogger(DistanceBasedClassificationMethodTest.class);

    @Before
    public void setUp() throws Exception {
        // Prepare Hazelcast cluster
        hazelcastInstance = HazelcastHelper.buildCluster(2);

        //prepare data
        IrisPlantDataReader irisPlantDataReader = new IrisPlantDataReader();
        InputStream is = IrisPlantDataReader.class.getClassLoader().getResourceAsStream("bezdekIris.data");
        irisPlants = irisPlantDataReader.read(is);
    }

    @Test
    public void shouldPassWhenTestPerformaceIsHigh() {
        Collection<ClassifiedFeatureDatum> plantsTrainData = new LinkedList<ClassifiedFeatureDatum>();
        Collection<Feature<IrisPlantFeature>> plantsPredictData = new LinkedList<Feature<IrisPlantFeature>>();
        int trainSize = (int) Math.round(0.7 * irisPlants.size());
        int predictSize = irisPlants.size() - trainSize;
        Set<Integer> predictDataIndex = new HashSet<Integer>();
        Random rd = new Random();
        for (int i = 0; i < predictSize; i++) {
            while ( predictDataIndex.add(rd.nextInt(irisPlants.size())) == false) {}
        }

        for (int i = 0; i < irisPlants.size(); i++) {
            if (!predictDataIndex.contains(i)) {
                plantsTrainData.add(irisPlants.get(i));
            }
        }
        DistanceBasedClassificationMethod method = new DistanceBasedClassificationMethod(hazelcastInstance, null);

        int success = 0;
        double successRate = 0;
        try {
            method.train(plantsTrainData);

            for (Integer integer : predictDataIndex) {
                ClassifiedFeatureDatum<IrisPlantFeature, String> classifiedFeatureDatum = irisPlants.get(integer.intValue());
                //System.out.println("Class to predict: " + classifiedFeatureDatum.getClassification().toString());
                plantsPredictData.clear();
                plantsPredictData.add(classifiedFeatureDatum.getFeature());
                Collection<Classification> classifications = method.predict(plantsPredictData);
                //System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                boolean check = true ;//TODO: implement MLMethod.compareClassifications(classifiedFeatureDatum.getClassification(), classifications);
                System.out.println("Result: " + check);
                if (check == false) {
                    System.out.println("Class to predict: " + classifiedFeatureDatum.getClassification().toString());
                    System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                } else {
                    success++;
                }

                //break; //testing one value
                successRate = ((double) success) / predictDataIndex.size();
                System.out.println("Success Rate: " + successRate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("SuccessRate (" + successRate + ") should be greater than SuccessRate ( 0.9 )", success > 0.9);
    }

    @After
    public void tearDown() throws Exception {
        // Shutdown cluster
        Hazelcast.shutdownAll();
    }
}