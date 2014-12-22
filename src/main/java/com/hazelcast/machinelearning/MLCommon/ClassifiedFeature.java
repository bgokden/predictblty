package com.hazelcast.machinelearning.MLCommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by berkgokden on 12/17/14.
 */
public class ClassifiedFeature implements DataSerializable, Serializable  {

    protected Classification classification;
    protected Feature feature;

    public ClassifiedFeature() {
    }

    public ClassifiedFeature(Feature feature, Classification classification) {
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

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        this.feature.writeData(out);
        this.classification.writeData(out);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.feature = new Feature();
        this.feature.readData(in);
        this.classification = new Classification();
        this.classification.readData(in);
    }
}
