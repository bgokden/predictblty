package com.hazelcast.machinelearning.methods.impl;

/**
 * Created by berkgokden on 9/9/14.
 */
public class ClassifiedFeatureDatum<T, S> {
    private Feature<T> feature;
    private Classification<S> classification;
    public ClassifiedFeatureDatum(Feature<T> feature, Classification<S> classification) {
        this.feature = feature;
        this.classification = classification;
    }

    public Feature<T> getFeature() {
        return feature;
    }

    public void setFeature(Feature<T> feature) {
        this.feature = feature;
    }

    public Classification<S> getClassification() {
        return classification;
    }

    public void setClassification(Classification<S> classification) {
        this.classification = classification;
    }
}
