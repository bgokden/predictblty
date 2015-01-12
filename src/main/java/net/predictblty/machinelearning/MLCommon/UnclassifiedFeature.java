package net.predictblty.machinelearning.mlcommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by berkgokden on 12/17/14.
 */
public class UnclassifiedFeature implements DataSerializable, Serializable {
    private static final long serialVersionUID = 1L;
    protected double confidence;
    protected Map<String, Serializable> featureMap;

    public UnclassifiedFeature() {
        this.featureMap = new ConcurrentHashMap<String, Serializable>();
        this.confidence = 1.0;
    }

    public UnclassifiedFeature(Map<String, Serializable> feature) {
        this.featureMap = feature;
        this.confidence = 1.0;
    }

    public UnclassifiedFeature(Map<String, Serializable> feature, double confidence) {
        this.featureMap = feature;
        this.confidence = confidence;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }



    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(featureMap);
        out.writeDouble(confidence);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.featureMap = in.readObject();
        this.confidence = in.readDouble();
    }

    public Map<String, Serializable> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Serializable> featureMap) {
        this.featureMap = featureMap;
    }
}
