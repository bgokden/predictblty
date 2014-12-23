package com.hazelcast.machinelearning.mlalgorithm.mlalgorithmimpl;


import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.machinelearning.mlalgorithm.MLAlgorithm;
import com.hazelcast.machinelearning.mlcommon.*;
import com.hazelcast.machinelearning.csv.IrisPlantDataReader;
import com.hazelcast.machinelearning.ToStringPrettyfier;
import com.hazelcast.machinelearning.model.IrisPlant;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

public class DistanceBasedClassificationAlgorithmTest {

    @Test
    public void shouldPassWhenSuccessIsHigh() throws Exception {
        Hazelcast.shutdownAll();
        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = HelpfulMethods.buildCluster(4);

        System.out.println("Cluster ready");

        try {
            //Read data
            IrisPlantDataReader irisPlantDataReader = new IrisPlantDataReader();
            InputStream is = IrisPlantDataReader.class.getClassLoader().getResourceAsStream("bezdekIris.data");
            List<IrisPlant> irisPlants = irisPlantDataReader.read(is);

            //Prepare training data and prediction data


            //70% for training 30% for prediction
            int trainSize = (int) Math.round(0.7 * irisPlants.size());
            int predictSize = irisPlants.size() - trainSize;
            Set<Integer> predictDataIndex = new HashSet<Integer>();
            Random rd = new Random();
            for (int i = 0; i < predictSize; i++) {
                while ( predictDataIndex.add(rd.nextInt(irisPlants.size())) == false) {}
            }
            Collection<IrisPlant> plantsTrainData = new ArrayList<IrisPlant>(trainSize);
            for (int i = 0; i < irisPlants.size(); i++) {
                if (!predictDataIndex.contains(i)) {
                    plantsTrainData.add(irisPlants.get(i));
                }
            }

            Map<String, Object> options = new HashMap<String, Object>();
            options.put("comparator", new FeatureComparators.DoubleManhattanDistanceFeatureComparator());
            MLAlgorithm algorithm = new DistanceBasedClassificationAlgorithm(hazelcastInstance, options);
            algorithm.train(plantsTrainData);

            int success = 0;

            for (Integer integer : predictDataIndex) {
                IrisPlant irisPlantToPredict = irisPlants.get(integer.intValue());
                Collection<IrisPlant> plantsPredictData = new ArrayList<IrisPlant>(1);
                plantsPredictData.add(irisPlantToPredict);
                Collection<Classification> classifications = algorithm.predict(plantsPredictData);
                double successPartial = HelpfulMethods.compareClassificationsWithClass(classifications, irisPlantToPredict.getPlantClass());
                System.out.println("Result: " + successPartial);
                success+=successPartial;
                if (successPartial == 0) {
                    System.out.println("Class to predict: " + irisPlantToPredict.getPlantClass());
                    System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                }
            }
            double successRate = ((double) success) / predictDataIndex.size();
            System.out.println("Success Rate: " + successRate);
            Assert.assertTrue(successRate >= 0.6);
        } finally {
            // Shutdown cluster
            Hazelcast.shutdownAll();
        }
    }



}