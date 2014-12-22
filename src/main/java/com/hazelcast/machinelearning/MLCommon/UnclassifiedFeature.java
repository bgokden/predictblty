package com.hazelcast.machinelearning.MLCommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by berkgokden on 12/17/14.
 */
public class UnclassifiedFeature implements DataSerializable, Serializable {

    protected Feature feature;
    protected double confidence;

    public UnclassifiedFeature() {
        this.feature = new Feature();
        this.confidence = 1.0;
    }

    public UnclassifiedFeature(Feature feature) {
        this.feature = feature;
        this.confidence = 1.0;
    }

    public UnclassifiedFeature(Feature feature, double confidence) {
        this.feature = feature;
        this.confidence = confidence;
    }


    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
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
        out.writeDouble(confidence);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.feature = new Feature();
        this.feature.readData(in);
        this.confidence = in.readDouble();
    }
}
