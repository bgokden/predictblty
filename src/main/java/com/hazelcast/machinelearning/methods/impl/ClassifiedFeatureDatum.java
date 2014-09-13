package com.hazelcast.machinelearning.methods.impl;

import java.util.Collection;

/**
 * Created by berkgokden on 9/9/14.
 */
public class ClassifiedFeatureDatum {
    private Classification classification;
    private Comparable feature;

    public ClassifiedFeatureDatum(Comparable feature, Classification classification) {
        this.feature = feature;
        this.classification = classification;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Comparable getFeature() {
        return feature;
    }

    public void setFeature(Comparable feature) {
        this.feature = feature;
    }
}
