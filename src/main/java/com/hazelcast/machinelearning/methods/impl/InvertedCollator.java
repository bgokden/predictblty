package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

/**
 * Created by berkgokden on 9/9/14.
 */
public class InvertedCollator implements Collator<Map.Entry<String, Double>, List<KeyValueTuple>> {

    @Override
    public List<KeyValueTuple> collate(Iterable<Map.Entry<String, Double>> values) {
        List<KeyValueTuple> result = new ArrayList<>();
        for (Map.Entry<String, Double> value : values) {
            result.add(new KeyValueTuple(value.getKey(), value.getValue()));
        }
        Collections.sort(result, new KeyValueTupleComparator());
        return result;
    }

    private static class KeyValueTupleComparator
            implements Comparator<KeyValueTuple> {

        @Override
        public int compare(KeyValueTuple o1, KeyValueTuple o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }
}
