package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.mapreduce.Reducer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by berkgokden on 9/19/14.
 */
public class DistanceBasedClassificationMethodReducerFactory implements com.hazelcast.mapreduce.ReducerFactory<Feature, ClassificationListWrapper, List<Classification>> {
    private Integer limit;
    public DistanceBasedClassificationMethodReducerFactory(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public Reducer<ClassificationListWrapper, List<Classification>> newReducer(Feature key) {
        return new DistanceBasedClassificationMethodReducer(limit);
    }

    private static class DistanceBasedClassificationMethodReducer
            extends Reducer<ClassificationListWrapper, List<Classification>> {

        private ConcurrentSkipListSet<Classification> classifications = null;
        private Integer limit;

        public DistanceBasedClassificationMethodReducer(Integer limit) {
            this.limit = limit;
            this.classifications = new ConcurrentSkipListSet<Classification>(); //TODO implement (new Classification.ClassificationComparator());
        }

        @Override
        public void reduce(ClassificationListWrapper classifications) {
            this.classifications.addAll(classifications.getClassificationList());
        }

        public void reduce(Classification classifications) {
            this.classifications.add(classifications);
        }

        @Override
        public List<Classification> finalizeReduce() {
            //Since capacity is fixed ArrayList is a good choice
            List<Classification> classificationsToReturn = new ArrayList<Classification>(this.limit);

            int i = 0;
            Iterator<Classification> iterator = this.classifications.iterator();
            while (iterator.hasNext() && i++ <= this.limit) {
                classificationsToReturn.add(iterator.next());
            }
            this.classifications.clear();
            return classificationsToReturn;
        }
    }
}
