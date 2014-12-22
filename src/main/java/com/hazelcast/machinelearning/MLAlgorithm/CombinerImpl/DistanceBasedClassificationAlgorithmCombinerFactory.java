package com.hazelcast.machinelearning.MLAlgorithm.CombinerImpl;

import com.hazelcast.machinelearning.MLCommon.Feature;
import com.hazelcast.machinelearning.MLCommon.Classification;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by berkgokden on 9/17/14.
 */
public class DistanceBasedClassificationAlgorithmCombinerFactory implements CombinerFactory<Map<String, Serializable>, Classification, List<Classification>> {
    private Integer limit;

    public DistanceBasedClassificationAlgorithmCombinerFactory(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public Combiner<Classification, List<Classification>> newCombiner(Map<String, Serializable> key) {
        return new DistanceBasedClassificationAlgorithmCombiner(limit);
    }

    private static class DistanceBasedClassificationAlgorithmCombiner
            extends Combiner<Classification, List<Classification>> {

        private Integer limit;
        private ConcurrentSkipListSet<Classification> classifications = null;

        public DistanceBasedClassificationAlgorithmCombiner(Integer limit) {
            this.limit = limit;
            this.classifications = new ConcurrentSkipListSet<Classification>(new Classification.ClassificationComparator());
        }


        @Override
        public void combine(Classification classification) {
            this.classifications.add(classification);
            //System.out.println("Combine :"+classification.toString());
        }

        @Override
        public List<Classification> finalizeChunk() {
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
