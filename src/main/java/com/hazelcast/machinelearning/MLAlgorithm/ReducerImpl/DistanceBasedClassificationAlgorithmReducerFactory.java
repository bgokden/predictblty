package com.hazelcast.machinelearning.MLAlgorithm.ReducerImpl;

import com.hazelcast.machinelearning.MLCommon.Classification;
import com.hazelcast.machinelearning.MLCommon.ClassificationListWrapper;
import com.hazelcast.machinelearning.MLCommon.Feature;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by berkgokden on 9/19/14.
 */
public class DistanceBasedClassificationAlgorithmReducerFactory implements ReducerFactory<Feature, ClassificationListWrapper, List<Classification>> {
    private Integer limit;
    public DistanceBasedClassificationAlgorithmReducerFactory(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public Reducer<ClassificationListWrapper, List<Classification>> newReducer(Feature key) {
        return new DistanceBasedClassificationAlgorithmReducer(limit);
    }

    private static class DistanceBasedClassificationAlgorithmReducer
            extends Reducer<ClassificationListWrapper, List<Classification>> {

        private ConcurrentSkipListSet<Classification> classifications = null;
        private Integer limit;

        public DistanceBasedClassificationAlgorithmReducer(Integer limit) {
            this.limit = limit;
            this.classifications = new ConcurrentSkipListSet<Classification>(new Classification.ClassificationComparator());
        }

        @Override
        public void reduce(ClassificationListWrapper classifications) {
            this.classifications.addAll(classifications.getClassificationList());
            System.out.println("Reduce :");
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
