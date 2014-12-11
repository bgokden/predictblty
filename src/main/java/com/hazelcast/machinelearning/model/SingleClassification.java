package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.Map;

/**
 * Created by berkgokden on 10/13/14.
 */
public class SingleClassification<S> implements Classification<S>,Comparable<SingleClassification<S>> {
    protected S classification;
    protected Double confidenceCoefficient;

    public SingleClassification(S classification, Double confidence) {
        this.classification = classification;
        this.confidenceCoefficient = confidence;
    }

    @Override
    public void addClassification(S classification, Double confidence) {
        this.classification = classification;
        this.confidenceCoefficient = confidence;
    }

    @Override
    public Double getConfidenceForClassification(S classification) {
        return null;
    }

    @Override
    public Map<S, Double> getClassifications() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public S getComparableClassification() {
        return this.classification;
    }

    @Override
    public Double getConfidenceCoefficient() {
        return this.confidenceCoefficient;
    }

    @Override
    public void setComparableClassification(S classification) {
        this.classification = classification;
    }

    @Override
    public void setConfidenceCoefficient(Double confidenceCoefficient) {
        this.confidenceCoefficient = confidenceCoefficient;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {

    }

    @Override
    public int compareTo(SingleClassification<S> o) {
        return Double.compare(o.getConfidenceCoefficient(), this.getConfidenceCoefficient());
    }
}
