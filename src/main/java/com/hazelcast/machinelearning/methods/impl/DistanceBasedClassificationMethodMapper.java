package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.machinelearning.model.SingleClassification;
import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.*;

/**
 * Created by berkgokden on 9/16/14.
 */
public class DistanceBasedClassificationMethodMapper<T, S> implements Mapper<Feature<T>, Classification<S>, Feature<T>, Classification<S>>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<Feature<T>> data;

    public DistanceBasedClassificationMethodMapper(Map<String, Object> options,Collection<Feature<T>> data) {
        this.data = data;
    }

    @Override
    public void map(Feature<T> key, Classification<S> value, Context<Feature<T>, Classification<S>> context) {
        for (Feature<T> feature : this.data) {

            double distance = feature.distanceTo(key);
            if (distance == 0) {
                distance = 1E-16;//kind of epsilon
            }

            double weight = feature.getConfidence() / distance;

            for (Map.Entry<S, Double> entry : value.getClassifications().entrySet()) {
                context.emit(feature, new SingleClassification<S>(entry.getKey(), entry.getValue()*weight));
            }

            //context.emit(feature, new Classification(value.getComparableClassification(), weight));
        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
