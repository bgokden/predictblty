package net.predictblty.machinelearning.methods.impl;

/**
 * Created by berkgokden on 9/9/14.
 */
public class ClassifiedFeatureDatum {
    protected Classification classification;
    protected Feature feature;

    public ClassifiedFeatureDatum() {
    }

    public ClassifiedFeatureDatum(Feature feature, Classification classification) {
        this.feature = feature;
        this.classification = classification;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
}
