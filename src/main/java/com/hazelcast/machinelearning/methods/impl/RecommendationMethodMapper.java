package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Collection;

/**
 * Created by berkgokden on 9/13/14.
 */
public class RecommendationMethodMapper implements Mapper<Comparable, Double, Feature, Double>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private String mapKey;

    public RecommendationMethodMapper(String mapKey) {
        this.mapKey = mapKey;
    }

    @Override
    public void map(Comparable key, Double value, Context<Feature, Double> context) {
        MultiMap<Comparable, FeatureConfidenceTuple> multiMap = hazelcastInstance.getMultiMap("traindata-"+this.mapKey);

        Collection<FeatureConfidenceTuple> values = multiMap.get(key);

        for (FeatureConfidenceTuple featureConfidenceTuple : values) {
            context.emit(featureConfidenceTuple.getFeature(), (featureConfidenceTuple.getConfidenceCoefficient() * value.doubleValue()));
        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
