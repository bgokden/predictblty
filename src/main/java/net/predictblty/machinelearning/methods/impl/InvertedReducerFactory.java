package net.predictblty.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

/**
 * Created by berkgokden on 9/9/14.
 */
public class InvertedReducerFactory implements ReducerFactory<String, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(String key) {
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
