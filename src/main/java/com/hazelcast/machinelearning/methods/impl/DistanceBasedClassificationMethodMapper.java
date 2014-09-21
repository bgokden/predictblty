package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.*;

/**
 * Created by berkgokden on 9/16/14.
 */
public class DistanceBasedClassificationMethodMapper implements Mapper<Feature, Classification, Feature, Classification>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<FeatureConfidenceTuple> data;

    public DistanceBasedClassificationMethodMapper(Map<String, Object> options,Collection<FeatureConfidenceTuple> data) {
        this.data = data;
    }

    @Override
    public void map(Feature key, Classification value, Context<Feature, Classification> context) {
        for (FeatureConfidenceTuple featureConfidenceTuple : this.data) {

            double distance = featureConfidenceTuple.getFeature().distanceTo(key);
            if (distance == 0) {
                distance = 1E-16;//kind of epsilon
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
