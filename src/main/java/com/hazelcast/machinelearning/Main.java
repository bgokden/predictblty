package com.hazelcast.machinelearning;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.machinelearning.csv.IrisPlantDataReader;
import com.hazelcast.machinelearning.methods.DistanceBasedClassificationMethod;
import com.hazelcast.machinelearning.methods.RecommendationMethod;
import com.hazelcast.machinelearning.methods.SparseMatrixMultiplication;
import com.hazelcast.machinelearning.methods.impl.*;
import com.hazelcast.machinelearning.model.IrisPlant;

import java.io.InputStream;
import java.util.*;

/**
 * Created by berkgokden on 9/9/14.
 */
public class Main {

    public static void main(String[] args)
            throws Exception {

        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = buildCluster(1);

        try {
            IrisPlantDataReader irisPlantDataReader = new IrisPlantDataReader();
            InputStream is = IrisPlantDataReader.class.getClassLoader().getResourceAsStream("bezdekIris.data");
            List<IrisPlant> irisPlants = irisPlantDataReader.read(is);
            Collection<ClassifiedFeatureDatum> plantsTrainData = new LinkedList<ClassifiedFeatureDatum>();
            Collection<FeatureConfidenceTuple> plantsPredictData = new LinkedList<FeatureConfidenceTuple>();
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
            method.train(plantsTrainData);

            int success = 0;

            for (Integer integer : predictDataIndex) {
                ClassifiedFeatureDatum classifiedFeatureDatum = irisPlants.get(integer.intValue());
                //System.out.println("Class to predict: " + classifiedFeatureDatum.getClassification().toString());
                FeatureConfidenceTuple featureConfidenceTuple = new FeatureConfidenceTuple(classifiedFeatureDatum.getFeature(), 1.0);
                plantsPredictData.clear();
                plantsPredictData.add(featureConfidenceTuple);
                Collection<Classification> classifications = method.predict(plantsPredictData);
                //System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                boolean check = compareClassifications(classifiedFeatureDatum.getClassification(),classifications);
                System.out.println("Result: " + check);
                if (check == false) {
                    System.out.println("Class to predict: " + classifiedFeatureDatum.getClassification().toString());
                    System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                } else {
                    success++;
                }

                //break; //testing one value
            }
            double successRate = ((double) success) / predictDataIndex.size();
            System.out.println("Success Rate: " + successRate);





            //SparseMatrixMultiplication method = new SparseMatrixMultiplication();
            //method.execute(hazelcastInstance);

//            RecommendationMethod recommendationMethod = new RecommendationMethod(hazelcastInstance);
//            List<ClassifiedFeatureDatum> trainingdata = new ArrayList<ClassifiedFeatureDatum>();
//            trainingdata.add(new ClassifiedFeatureDatum("EventA", new Classification("ResultB",1.0)));
//            trainingdata.add(new ClassifiedFeatureDatum("EventA", new Classification("ResultB",1.0)));
//            trainingdata.add(new ClassifiedFeatureDatum("EventA", new Classification("ResultC")));
//            trainingdata.add(new ClassifiedFeatureDatum("EventB", new Classification("ResultA")));
//            trainingdata.add(new ClassifiedFeatureDatum("EventB", new Classification("ResultC")));
//            trainingdata.add(new ClassifiedFeatureDatum("EventC", new Classification("ResultA")));
//            trainingdata.add(new ClassifiedFeatureDatum("EventC", new Classification("ResultB")));
//
//            recommendationMethod.train(trainingdata);
//            List<FeatureConfidenceTuple> predictdata = new ArrayList<FeatureConfidenceTuple>();
//            predictdata.add(new FeatureConfidenceTuple("EventA",1.0));
//            predictdata.add(new FeatureConfidenceTuple("EventB",1.5));
//
//            Collection<Classification> classifications = recommendationMethod.predict(predictdata);
//            System.out.println("Result: " + ToStringPrettyfier.toString(classifications));


        } finally {
            // Shutdown cluster
            Hazelcast.shutdownAll();
        }
    }


    /*
    * Build cluster method taken from hazelcast-mapreduce-presentation
    * */
    private static HazelcastInstance buildCluster(int memberCount) {
        Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.getJoin().getTcpIpConfig().setMembers(Arrays.asList(new String[]{"127.0.0.1"}));

        HazelcastInstance[] hazelcastInstances = new HazelcastInstance[memberCount];
        for (int i = 0; i < memberCount; i++) {
            hazelcastInstances[i] = Hazelcast.newHazelcastInstance(config);
        }
        return hazelcastInstances[0];
    }

    private static boolean compareClassifications(Classification classification, Collection<Classification> classifications) {
        for (Classification classification1 : classifications) {
            if (classification1.getComparableClassification().equals(classification.getComparableClassification())) {
                return true;
            }
            break;
        }
        return false;
    }
}
