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

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 12/17/14.
 */
public class DistanceBasedClassificationAlgorithm extends MLAlgorithm {

    private IMap<Feature, Classification> trainingdata;

    public DistanceBasedClassificationAlgorithm(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        super(hazelcastInstance, options);
        this.options.putIfAbsent("limit", 10);
        this.options.putIfAbsent("order", 1);
        this.options.putIfAbsent("comparator", new FeatureComparators.DoubleFeatureComparator());
        this.trainingdata = hazelcastInstance.getMap("traindata-" + this.getDataId());
    }

    @Override
    public void train(Collection<ClassifiedFeature> data) throws Exception {
        for (ClassifiedFeature classifiedFeature : data) {
            this.trainingdata.set(classifiedFeature.getFeature(), classifiedFeature.getClassification());
        }
    }

    @Override
    public Collection<Classification> predict(Collection<UnclassifiedFeature> data) throws Exception {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        KeyValueSource<Feature, Classification> source = KeyValueSource.fromMap(this.trainingdata);

        Job<Feature, Classification> job = jobTracker.newJob(source);

        JobCompletableFuture<List<Classification>> future = job //
                .mapper(new DistanceBasedClassificationAlgorithmMapper(this.options,data)) //
                .combiner(new DistanceBasedClassificationAlgorithmCombinerFactory(this.options)) //
                .reducer(new DistanceBasedClassificationAlgorithmReducerFactory(this.options)) //
                .submit(new DistanceBasedClassificationAlgorithmCollator(this.options));

        //System.out.println("Result: " + ToStringPrettyfier.toString(future.get()));
        return future.get();
    }
}
