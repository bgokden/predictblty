package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by berkgokden on 9/13/14.
 */
public class Classification implements DataSerializable{

    private double confidenceCoefficient;
    private Comparable comparableClassification;

    public Classification() {}

    public Classification(Comparable comparableClassification, Double confidenceCoefficient) {
        this.comparableClassification = comparableClassification;
        this.confidenceCoefficient = confidenceCoefficient.doubleValue();
    }

    public Classification(Comparable comparableClassification, double confidenceCoefficient) {
        this.comparableClassification = comparableClassification;
        this.confidenceCoefficient = confidenceCoefficient;
    }

    public Classification(Comparable comparableClassification) {
        this.comparableClassification = comparableClassification;
        this.confidenceCoefficient = 1.0;
    }

    public double getConfidenceCoefficient() {
        return confidenceCoefficient;
    }

    public void setConfidenceCoefficient(double confidenceCoefficient) {
        this.confidenceCoefficient = confidenceCoefficient;
    }

    public Comparable getComparableClassification() {
        return comparableClassification;
    }

    public void setComparableClassification(Comparable comparableClassification) {
        this.comparableClassification = comparableClassification;
    }

    public String toString() {
        return this.comparableClassification.toString()+" "+this.confidenceCoefficient;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(this.comparableClassification);
        out.writeDouble(this.confidenceCoefficient);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.comparableClassification = in.readObject();
        this.confidenceCoefficient = in.readDouble();
    }

    public static class ClassificationComparator
            implements Comparator<Classification> {

        @Override
        public int compare(Classification o1, Classification o2) {
            return Double.compare(o2.getConfidenceCoefficient(), o1.getConfidenceCoefficient());
        }
    }

}
