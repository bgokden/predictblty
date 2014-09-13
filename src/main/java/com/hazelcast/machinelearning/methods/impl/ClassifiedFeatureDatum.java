package com.hazelcast.machinelearning.methods.impl;

import java.util.Collection;

/**
 * Created by berkgokden on 9/9/14.
 */
public interface ClassifiedFeatureDatum {
    Classification getClassification();
    Collection<Comparable> getFeatures();
}
