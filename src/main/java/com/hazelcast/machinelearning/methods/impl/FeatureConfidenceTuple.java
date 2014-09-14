package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by berkgokden on 9/13/14.
 */
public class FeatureConfidenceTuple implements DataSerializable {
    private Comparable feature;
    private double confidenceCoefficent;

    public FeatureConfidenceTuple(){}

    public FeatureConfidenceTuple(Comparable feature, double confidenceCoefficient) {
        this.feature = feature;
        this.confidenceCoefficent = confidenceCoefficient;
    }

    public Comparable getFeature() {
        return feature;
    }

    public void setFeature(Comparable feature) {
        this.feature = feature;
    }

    public double getConfidenceCoefficent() {
        return confidenceCoefficent;
    }

    public void setConfidenceCoefficent(double confidenceCoefficent) {
        this.confidenceCoefficent = confidenceCoefficent;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeObject(feature);
        objectDataOutput.writeDouble(confidenceCoefficent);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.feature = objectDataInput.readObject();
        this.confidenceCoefficent = objectDataInput.readDouble();
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
