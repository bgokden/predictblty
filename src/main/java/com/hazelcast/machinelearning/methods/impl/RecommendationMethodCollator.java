package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

/**
 * Created by berkgokden on 9/13/14.
 */
public class RecommendationMethodCollator implements Collator<Map.Entry<Feature, Double>, List<Classification>> {
    @Override
    public List<Classification> collate(Iterable<Map.Entry<Feature, Double>> values) {
        List<Classification> result = new ArrayList<Classification>();
        double sum = 0;
        for (Map.Entry<Feature, Double> value : values) {
            sum += value.getValue();
            //TODO: result.add(new Classification(value.getKey(), value.getValue()));
        }
        Collections.sort(result, new ClassificationComparator());
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setConfidenceCoefficient(result.get(i).getConfidenceCoefficient() / sum);
        }
        return result;
    }

    private static class ClassificationComparator
            implements Comparator<Classification> {

        @Override
        public int compare(Classification o1, Classification o2) {
            return Double.compare(o2.getConfidenceCoefficient(), o1.getConfidenceCoefficient());
        }
    }
}
