package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

/**
 * Created by berkgokden on 9/13/14.
 */
public class RecommendationMethodReducerFactory
        implements ReducerFactory<Comparable, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(Comparable key) {
        return new InvertedReducer();
    }

    private static class InvertedReducer
            extends Reducer<Double, Double> {

        private volatile double sum;

        @Override
        public void reduce(Double value) {
            sum += value;
        }

        @Override
        public Double finalizeReduce() {
            return sum;
        }
    }
}
