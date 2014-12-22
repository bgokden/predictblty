package com.hazelcast.machinelearning.MLAlgorithm.MLAlgorithmImpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.machinelearning.MLAlgorithm.CollatorImpl.DistanceBasedClassificationAlgorithmCollator;
import com.hazelcast.machinelearning.MLAlgorithm.MLAlgorithm;
import com.hazelcast.machinelearning.MLAlgorithm.CombinerImpl.DistanceBasedClassificationAlgorithmCombinerFactory;
import com.hazelcast.machinelearning.MLAlgorithm.MapperImpl.DistanceBasedClassificationAlgorithmMapper;
import com.hazelcast.machinelearning.MLAlgorithm.ReducerImpl.DistanceBasedClassificationAlgorithmReducerFactory;
import com.hazelcast.machinelearning.MLCommon.*;
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
        this.options.putIfAbsent("limit", 10);
        this.options.putIfAbsent("order", 1);
        this.options.putIfAbsent("comparator", new FeatureComparators.DoubleFeatureComparator());
    }

    public DistanceBasedClassificationAlgorithm(HazelcastInstance hazelcastInstance) {
        this(hazelcastInstance, null);
    }


    @Override
    public void train(Collection<ClassifiedFeature> data) throws Exception {
        this.trainingdata = getTrainingdata();
        for (ClassifiedFeature classifiedFeature : data) {
            this.trainingdata.set(classifiedFeature.getFeatureMap(), classifiedFeature.getClassification());
        }
    }

    @Override
    public Collection<Classification> predict(Collection<UnclassifiedFeature> data) throws Exception {
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
