package net.predictblty.machinelearning.mlalgorithm.mlalgorithmimpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import net.predictblty.machinelearning.mlalgorithm.MLAlgorithm;
import net.predictblty.machinelearning.mlalgorithm.collatorimpl.DistanceBasedClassificationAlgorithmCollator;
import net.predictblty.machinelearning.mlalgorithm.combinerimpl.DistanceBasedClassificationAlgorithmCombinerFactory;
import net.predictblty.machinelearning.mlalgorithm.mapperimpl.UserBasedCollaborativeFilteringRecommendationAlgorithmMapper;
import net.predictblty.machinelearning.mlalgorithm.reducerimpl.DistanceBasedClassificationAlgorithmReducerFactory;
import net.predictblty.machinelearning.mlcommon.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 1/12/15.
 */
public class UserBasedCollaborativeFilteringRecommendationAlgorithm extends MLAlgorithm {
    private IMap<ByteHolder, ClassifiedFeature> trainingdata;
    private static final int LIMIT = 10;
    private static final int DESC = 1;

    public UserBasedCollaborativeFilteringRecommendationAlgorithm(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        super(hazelcastInstance, options);
        if (!this.options.containsKey("limit")) {
            this.options.put("limit", LIMIT);
        }
        if (!this.options.containsKey("order")) {
            this.options.put("order", DESC);
        }
        if (!this.options.containsKey("comparator")) {
            this.options.put("comparator", new FeatureComparators.DoubleEuclideanDistanceFeatureComparator());
        }
    }

    public UserBasedCollaborativeFilteringRecommendationAlgorithm(HazelcastInstance hazelcastInstance) {
        this(hazelcastInstance, null);
    }


    @Override
    public void train(Collection<? extends Object> data) throws Exception {
        this.trainingdata = getTrainingdata();
        for (Object object : data) {
            ClassifiedFeature classifiedFeature = Reflections.getClassifiedFeatureFromObject(object);
            ByteHolder key = HelpfulMethods.generateSortedFetureByteArray(classifiedFeature.getFeatureMap());
            this.trainingdata.executeOnKey(key, new ClassifiedFeatureIncByEntryProcessor(classifiedFeature));
            //this.trainingdata.set(classifiedFeature.getFeatureMap(), classifiedFeature.getClassification());
        }
    }

    @Override
    public Collection<Classification> predict(Collection<? extends Object> data) throws Exception {
        this.trainingdata = getTrainingdata();
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        KeyValueSource<ByteHolder, ClassifiedFeature> source = KeyValueSource.fromMap(this.trainingdata);

        Job<ByteHolder, ClassifiedFeature> job = jobTracker.newJob(source);
//
        JobCompletableFuture<List<Classification>> future = job //
                .mapper(new UserBasedCollaborativeFilteringRecommendationAlgorithmMapper(this.options,data)) //
                .combiner(new DistanceBasedClassificationAlgorithmCombinerFactory(this.options)) //
                .reducer(new DistanceBasedClassificationAlgorithmReducerFactory(this.options)) //
                .submit(new DistanceBasedClassificationAlgorithmCollator(this.options));

        return future.get();
    }

    public IMap<ByteHolder, ClassifiedFeature> getTrainingdata() {
        if (trainingdata == null) {
            this.trainingdata = hazelcastInstance.getMap(this.getPrefix()+"-traindata-" + this.getDataId());
        }
        return trainingdata;
    }

    public void setTrainingdata(IMap<ByteHolder, ClassifiedFeature> trainingdata) {
        this.trainingdata = trainingdata;
    }

    public static class ClassifiedFeatureIncByEntryProcessor extends AbstractEntryProcessor<ByteHolder, ClassifiedFeature> {
        private ClassifiedFeature value;
        public ClassifiedFeatureIncByEntryProcessor(ClassifiedFeature value) {
            super();
            this.value = value;
        }

        @Override
        public Object process(Map.Entry<ByteHolder, ClassifiedFeature> entry) {
            if (entry.getValue() == null) {
                if (value.getClassification().getConfidence() != 0) {
                    entry.setValue(value);
                }
            } else {
                double oldValue = entry.getValue().getClassification().getConfidence();
                double newValue = oldValue + value.getClassification().getConfidence();
                entry.getValue().getClassification().setConfidence(newValue);
                entry.setValue(entry.getValue());
            }
            return null;
        }
    }
}
