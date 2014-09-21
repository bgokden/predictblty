package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by berkgokden on 9/17/14.
 */
public class DistanceBasedClassificationMethodCombinerFactory implements CombinerFactory<Feature, Classification, ClassificationListWrapper> {
    private Integer limit;

    public DistanceBasedClassificationMethodCombinerFactory(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public Combiner<Classification, ClassificationListWrapper> newCombiner(Feature key) {
        return new DistanceBasedClassificationMethodCombiner(limit);
    }

    private static class DistanceBasedClassificationMethodCombiner
            extends Combiner<Classification, ClassificationListWrapper> {

        private Integer limit;
        private ConcurrentSkipListSet<Classification> classifications = null;

        public DistanceBasedClassificationMethodCombiner(Integer limit) {
            this.limit = limit;
            this.classifications = new ConcurrentSkipListSet<Classification>(new Classification.ClassificationComparator());
        }


        @Override
        public void combine(Classification classification) {
            this.classifications.add(classification);
        }

        @Override
        public ClassificationListWrapper finalizeChunk() {
            //Since capacity is fixed ArrayList is a good choice
            List<Classification> classificationsToReturn = new ArrayList<Classification>(this.limit);

            int i = 0;
            Iterator<Classification> iterator = this.classifications.iterator();
            while (iterator.hasNext() && i++ <= this.limit) {
                classificationsToReturn.add(iterator.next());
            }
            this.classifications.clear();
            return new ClassificationListWrapper(classificationsToReturn);
        }

//        private static class ClassificationComparator
//                implements Comparator<Classification> {
//
//            @Override
//            public int compare(Classification o1, Classification o2) {
//                return Double.compare(o2.getConfidenceCoefficient(), o1.getConfidenceCoefficient());
//            }
//        }
    }
}
