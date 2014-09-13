package com.hazelcast.machinelearning;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.ClassifiedFeatureDatum;
import com.hazelcast.machinelearning.methods.impl.FeatureConfidenceTuple;

import java.util.Collection;

/**
 * Created by berkgokden on 9/9/14.
 */
public interface MLMethod {
    void train(Collection<ClassifiedFeatureDatum> data) throws Exception;
    Collection<Classification> predict(Collection<FeatureConfidenceTuple> data) throws Exception;
}
