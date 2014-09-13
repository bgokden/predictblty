package com.hazelcast.machinelearning.methods;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.machinelearning.MLMethod;
import com.hazelcast.machinelearning.methods.impl.*;
import com.hazelcast.mapreduce.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by berkgokden on 9/13/14.
 */
public class RecommendationMethod implements MLMethod {


    private transient HazelcastInstance hazelcastInstance;
    private MultiMap<Comparable, FeatureConfidenceTuple> multiMap;
    private String randomKey;

    public RecommendationMethod(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.randomKey = RandomStringUtils.random(20);

        this.multiMap = hazelcastInstance.getMultiMap("traindata-"+this.randomKey);
    }

    //@Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void train(Collection<ClassifiedFeatureDatum> data) throws Exception {
        for (ClassifiedFeatureDatum classifiedFeatureDatum : data) {
            this.multiMap.put(classifiedFeatureDatum.getFeature(), new FeatureConfidenceTuple(classifiedFeatureDatum.getClassification().getComparableClassification(), classifiedFeatureDatum.getClassification().getConfidenceCoefficient()));
        }
    }

    @Override
    public Collection<Classification> predict(Collection<FeatureConfidenceTuple> data) throws Exception {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");
        IMap<Comparable, Double> map = hazelcastInstance.getMap("predictdata-"+this.randomKey);

        for (FeatureConfidenceTuple featureConfidenceTuple : data) {
            map.put(featureConfidenceTuple.getFeature(), new Double(featureConfidenceTuple.getConfidenceCoefficent()));
        }


        KeyValueSource<Comparable, Double> source = KeyValueSource.fromMap(map);

        Job<Comparable, Double> job = jobTracker.newJob(source);

        JobCompletableFuture<List<Classification>> future = job //
                .mapper(new RecommendationMethodMapper(this.randomKey)) //
                .combiner(new RecommendationMethodCombinerFactory()) //
                .reducer(new RecommendationMethodReducerFactory()) //
                .submit(new RecommendationMethodCollator());

        //System.out.println("Result: " + ToStringPrettyfier.toString(future.get()));
        return future.get();
    }

}
