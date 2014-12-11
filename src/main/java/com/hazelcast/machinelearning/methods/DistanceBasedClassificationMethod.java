package com.hazelcast.machinelearning.methods;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.machinelearning.MLMethod;
import com.hazelcast.machinelearning.methods.impl.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 9/16/14.
 */
public class DistanceBasedClassificationMethod<T, S> extends MLMethod<T, S> {

    private IMap<Feature, Classification> trainingdata;

    public DistanceBasedClassificationMethod(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        super(hazelcastInstance, options);
        this.options.putIfAbsent("limit", 10);
        this.options.putIfAbsent("order", 1);
        this.trainingdata = hazelcastInstance.getMap("traindata-" + this.getDataId());
    }

    @Override
    public void train(Collection<ClassifiedFeatureDatum<T, S>> data) throws Exception {
        for (ClassifiedFeatureDatum classifiedFeatureDatum : data) {
            this.trainingdata.put(classifiedFeatureDatum.getFeature(), classifiedFeatureDatum.getClassification());
        }
    }

    @Override
    public Collection<Classification<S>> predict(Collection<Feature<T>> data) throws Exception {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        KeyValueSource<Feature, Classification> source = KeyValueSource.fromMap(this.trainingdata);

        Job<Feature, Classification> job = jobTracker.newJob(source);

        JobCompletableFuture<List<Classification<S>>> future = job //
                .mapper(new DistanceBasedClassificationMethodMapper(this.options,data)) //
                .combiner(new DistanceBasedClassificationMethodCombinerFactory(this.options)) //
                .reducer(new DistanceBasedClassificationMethodReducerFactory(this.options)) //
                .submit(new DistanceBasedClassificationMethodCollator(this.options));

        //System.out.println("Result: " + ToStringPrettyfier.toString(future.get()));
        return future.get();
    }
}
