package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

/**
 * Created by berkgokden on 9/9/14.
 */
public class InvertedCombinerFactory
        implements CombinerFactory<String, Double, Double> {

    @Override
    public Combiner<Double, Double> newCombiner(String key) {
        return new InvertedCombiner();
    }

    private static class InvertedCombiner
            extends Combiner<Double, Double> {

        private volatile double sum;

        @Override
        public void combine(Double value) {
            sum += value;
        }

        @Override
        public Double finalizeChunk() {
            double sum = this.sum;
            this.sum = 0;
            return sum;
        }
    }
}
