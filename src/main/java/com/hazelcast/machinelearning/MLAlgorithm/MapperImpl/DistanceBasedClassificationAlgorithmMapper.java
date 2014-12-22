package com.hazelcast.machinelearning.MLAlgorithm.MapperImpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.machinelearning.methods.impl.FeatureConfidenceTuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 9/16/14.
 */
public class DistanceBasedClassificationAlgorithmMapper implements Mapper<Feature, Classification, Feature, Classification>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<FeatureConfidenceTuple> data;

    public DistanceBasedClassificationAlgorithmMapper(Map<String, Object> options, Collection<FeatureConfidenceTuple> data) {
        this.data = data;
    }

    @Override
    public void map(Feature key, Classification value, Context<Feature, Classification> context) {
        for (FeatureConfidenceTuple featureConfidenceTuple : this.data) {

            double distance = featureConfidenceTuple.getFeature().distanceTo(key);
            if (distance == 0) {
                distance = Double.MIN_NORMAL;//kind of epsilon
            }

            double weight = featureConfidenceTuple.getConfidenceCoefficient() / distance;

            List<Classification> list = new ArrayList<Classification>();
            context.emit(featureConfidenceTuple.getFeature(), new Classification(value.getComparableClassification(), weight));

        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
