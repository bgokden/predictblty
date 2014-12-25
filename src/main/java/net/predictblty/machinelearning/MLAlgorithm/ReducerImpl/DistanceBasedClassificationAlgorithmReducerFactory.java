package net.predictblty.machinelearning.MLAlgorithm.ReducerImpl;

import net.predictblty.machinelearning.MLCommon.Classification;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by berkgokden on 9/19/14.
 */
public class DistanceBasedClassificationAlgorithmReducerFactory implements ReducerFactory<Map<String, Serializable>, List<Classification>, List<Classification>> {
    private Integer limit;
    public DistanceBasedClassificationAlgorithmReducerFactory(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public Reducer<List<Classification>, List<Classification>> newReducer(Map<String, Serializable> key) {
        return new DistanceBasedClassificationAlgorithmReducer(limit);
    }

    private static class DistanceBasedClassificationAlgorithmReducer
            extends Reducer<List<Classification>, List<Classification>> {

        private ConcurrentSkipListSet<Classification> classifications = null;
        private Integer limit;

        public DistanceBasedClassificationAlgorithmReducer(Integer limit) {
            this.limit = limit;
            this.classifications = new ConcurrentSkipListSet<Classification>(new Classification.ClassificationComparator());
        }

        @Override
        public void reduce(List<Classification> classifications) {
            this.classifications.addAll(classifications);
            //System.out.println("Reduce :");
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
