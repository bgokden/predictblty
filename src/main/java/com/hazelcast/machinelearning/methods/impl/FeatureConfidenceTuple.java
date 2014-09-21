package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by berkgokden on 9/13/14.
 */
public class FeatureConfidenceTuple implements DataSerializable {
    private Feature feature;
    private double confidenceCoefficient;

    public FeatureConfidenceTuple(){}

    //TODO this is created just for compatibility
    public FeatureConfidenceTuple(Comparable feature, double confidenceCoefficient) {
        this.feature = null;
        this.confidenceCoefficient = confidenceCoefficient;
    }

    public FeatureConfidenceTuple(Feature feature, double confidenceCoefficient) {
        this.feature = feature;
        this.confidenceCoefficient = confidenceCoefficient;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public double getConfidenceCoefficient() {
        return confidenceCoefficient;
    }

    public void setConfidenceCoefficient(double confidenceCoefficient) {
        this.confidenceCoefficient = confidenceCoefficient;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeObject(feature);
        objectDataOutput.writeDouble(confidenceCoefficient);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.feature = objectDataInput.readObject();
        this.confidenceCoefficient = objectDataInput.readDouble();
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode run");
        return this.feature.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        System.out.println("Equals run");
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        if (this.feature != null && this.feature.equals(((FeatureConfidenceTuple) other).getFeature())) {
            return true;
        }
        return false;
    }

}
