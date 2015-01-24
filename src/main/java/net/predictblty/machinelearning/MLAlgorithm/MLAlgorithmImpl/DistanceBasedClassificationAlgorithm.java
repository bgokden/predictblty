package net.predictblty.machinelearning.mlalgorithm.mlalgorithmimpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import net.predictblty.machinelearning.mlalgorithm.collatorimpl.DistanceBasedClassificationAlgorithmCollator;
import net.predictblty.machinelearning.mlalgorithm.combinerimpl.DistanceBasedClassificationAlgorithmCombinerFactory;
import net.predictblty.machinelearning.mlalgorithm.MLAlgorithm;
import net.predictblty.machinelearning.mlalgorithm.mapperimpl.DistanceBasedClassificationAlgorithmMapper;
import net.predictblty.machinelearning.mlalgorithm.reducerimpl.DistanceBasedClassificationAlgorithmReducerFactory;
import net.predictblty.machinelearning.mlcommon.Classification;
import net.predictblty.machinelearning.mlcommon.ClassifiedFeature;
import net.predictblty.machinelearning.mlcommon.FeatureComparators;
import net.predictblty.machinelearning.mlcommon.Reflections;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 12/17/14.
 */
public class DistanceBasedClassificationAlgorithm extends MLAlgorithm {

    private IMap<Map<String, Serializable>, Classification> trainingdata;
    private static final int LIMIT = 10;
    private static final int DESC = 1;

    public DistanceBasedClassificationAlgorithm(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
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

    public DistanceBasedClassificationAlgorithm(HazelcastInstance hazelcastInstance) {
        this(hazelcastInstance, null);
    }


    @Override
    public void train(Collection<? extends Object> data) throws Exception {
        this.trainingdata = getTrainingdata();
        for (Object object : data) {
            ClassifiedFeature classifiedFeature = Reflections.getClassifiedFeatureFromObject(object);
            this.trainingdata.set(classifiedFeature.getFeatureMap(), classifiedFeature.getClassification());
        }
    }

    @Override
    public Collection<Classification> predict(Collection<? extends Object> data) throws Exception {
        this.trainingdata = getTrainingdata();
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        KeyValueSource<Map<String, Serializable>, Classification> source = KeyValueSource.fromMap(this.trainingdata);

        Job<Map<String, Serializable>, Classification> job = jobTracker.newJob(source);

        JobCompletableFuture<List<Classification>> future = job //
                .mapper(new DistanceBasedClassificationAlgorithmMapper(this.options,data)) //
                .combiner(new DistanceBasedClassificationAlgorithmCombinerFactory(this.options)) //
                .reducer(new DistanceBasedClassificationAlgorithmReducerFactory(this.options)) //
                .submit(new DistanceBasedClassificationAlgorithmCollator(this.options));

        return future.get();
    }


    public IMap<Map<String, Serializable>, Classification> getTrainingdata() {
        if (trainingdata == null) {
            this.trainingdata = hazelcastInstance.getMap(this.getPrefix() +"-traindata-" + this.getDataId());
        }
        return trainingdata;
    }

    public void setTrainingdata(IMap<Map<String, Serializable>, Classification> trainingdata) {
        this.trainingdata = trainingdata;
    }
}
