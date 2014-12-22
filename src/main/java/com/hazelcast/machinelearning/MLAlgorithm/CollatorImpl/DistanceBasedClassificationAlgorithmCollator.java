package com.hazelcast.machinelearning.MLAlgorithm.CollatorImpl;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

/**
 * Created by berkgokden on 9/19/14.
 */
public class DistanceBasedClassificationAlgorithmCollator implements Collator<Map.Entry<Feature, List<Classification>>, List<Classification>> {
    Integer limit;
    public DistanceBasedClassificationAlgorithmCollator(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public List<Classification> collate(Iterable<Map.Entry<Feature, List<Classification>>> values) {
        Map<Comparable, Classification> classificationMap = new HashMap<Comparable, Classification>();
        double coefficient = 0;
        Classification temp = null;
        for (Map.Entry<Feature, List<Classification>> value : values) {
            List<Classification> classifications = value.getValue();
            for (int i = 0; i < classifications.size(); i++) {
                coefficient = classifications.get(i).getConfidenceCoefficient();
                temp = classificationMap.get(classifications.get(i).getComparableClassification());
                if (temp == null) {
                    temp = new Classification(classifications.get(i).getComparableClassification(), coefficient);
                } else {
                    temp.setConfidenceCoefficient(coefficient + temp.getConfidenceCoefficient());
                }
                classificationMap.put(classifications.get(i).getComparableClassification(), temp);
            }

        }
        List<Classification> result = new LinkedList<Classification>(classificationMap.values());
        Collections.sort(result, new Classification.ClassificationComparator());
        result = result.subList(0, Math.min(this.limit, result.size()));
        double sum = 0;
        for (Classification classification : result) {
            sum += classification.getConfidenceCoefficient();
        }
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setConfidenceCoefficient(result.get(i).getConfidenceCoefficient() / sum);
        }
        return result;
    }
}
