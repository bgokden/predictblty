package com.hazelcast.machinelearning.mlalgorithm.mlalgorithmimpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.machinelearning.mlalgorithm.collatorimpl.DistanceBasedClassificationAlgorithmCollator;
import com.hazelcast.machinelearning.mlalgorithm.MLAlgorithm;
import com.hazelcast.machinelearning.mlalgorithm.combinerimpl.DistanceBasedClassificationAlgorithmCombinerFactory;
import com.hazelcast.machinelearning.mlalgorithm.mapperimpl.DistanceBasedClassificationAlgorithmMapper;
import com.hazelcast.machinelearning.mlalgorithm.reducerimpl.DistanceBasedClassificationAlgorithmReducerFactory;
import com.hazelcast.machinelearning.mlcommon.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 12/17/14.
 */
public class DistanceBasedClassificationAlgorithm extends MLAlgorithm {

    private IMap<Map<String, Serializable>, Classification> trainingdata;

    public DistanceBasedClassificationAlgorithm(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        super(hazelcastInstance, options);
        if (!this.options.containsKey("limit")) this.options.put("limit", 10);
        if (!this.options.containsKey("order")) this.options.put("order", 1);
        if (!this.options.containsKey("comparator")) this.options.put("comparator", new FeatureComparators.DoubleEuclideanDistanceFeatureComparator());
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
            this.trainingdata = hazelcastInstance.getMap("traindata-" + this.getDataId());
        }
        return trainingdata;
    }

    public void setTrainingdata(IMap<Map<String, Serializable>, Classification> trainingdata) {
        this.trainingdata = trainingdata;
    }
}
