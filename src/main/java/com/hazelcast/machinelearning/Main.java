package com.hazelcast.machinelearning;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.hazelcast.machinelearning.methods.RecommendationMethod;
import com.hazelcast.machinelearning.methods.SparseMatrixMultiplication;
import com.hazelcast.machinelearning.methods.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by berkgokden on 9/9/14.
 */
public class Main {

    public static void main(String[] args)
            throws Exception {

        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = buildCluster(1);

        // Read CSV data
        //ReaderHelper.read(hazelcastInstance);

//        MultiMap<String, KeyValueTuple> multiMap = hazelcastInstance.getMultiMap("keyValueTuples");
//        multiMap.put("EventA", new KeyValueTuple("ResultB", Math.random() * 99 + 1));
//        multiMap.put("EventA", new KeyValueTuple("ResultC", Math.random() * 99 + 1));
//
//        multiMap.put("EventB", new KeyValueTuple("ResultA", Math.random() * 99 + 1));
//        multiMap.put("EventB", new KeyValueTuple("ResultC", Math.random() * 99 + 1));
//
//        multiMap.put("EventC", new KeyValueTuple("ResultA", Math.random() * 99 + 1));
//        multiMap.put("EventC", new KeyValueTuple("ResultB", Math.random() * 99 + 1));

        try {
            //SparseMatrixMultiplication method = new SparseMatrixMultiplication();
            //method.execute(hazelcastInstance);

            RecommendationMethod recommendationMethod = new RecommendationMethod(hazelcastInstance);
            List<ClassifiedFeatureDatum> trainingdata = new ArrayList<ClassifiedFeatureDatum>();
            trainingdata.add(new ClassifiedFeatureDatum("EventA", new Classification("ResultB",1.0)));
            trainingdata.add(new ClassifiedFeatureDatum("EventA", new Classification("ResultC")));
            trainingdata.add(new ClassifiedFeatureDatum("EventB", new Classification("ResultA")));
            trainingdata.add(new ClassifiedFeatureDatum("EventB", new Classification("ResultC")));
            trainingdata.add(new ClassifiedFeatureDatum("EventC", new Classification("ResultA")));
            trainingdata.add(new ClassifiedFeatureDatum("EventC", new Classification("ResultB")));

            recommendationMethod.train(trainingdata);
            List<FeatureConfidenceTuple> predictdata = new ArrayList<FeatureConfidenceTuple>();
            predictdata.add(new FeatureConfidenceTuple("EventA",1.0));
            predictdata.add(new FeatureConfidenceTuple("EventB",1.5));

            Collection<Classification> classifications = recommendationMethod.predict(predictdata);
            System.out.println("Result: " + ToStringPrettyfier.toString(classifications));


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
}
