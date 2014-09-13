package com.hazelcast.machinelearning.methods.impl;

/**
 * Created by berkgokden on 9/13/14.
 */
public class Classification {

    private double confidenceCoefficient;
    private Comparable comparableClassification;

    public Classification(Comparable comparableClassification, Double confidenceCoefficient) {
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
}
