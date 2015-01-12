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
public class ClassifiedFeature implements DataSerializable {

    protected Classification classification;
    protected Map<String, Serializable> featureMap;

    public ClassifiedFeature() {
        this.featureMap = new ConcurrentHashMap<String, Serializable>();
        this.classification = new Classification();
    }

    public ClassifiedFeature(Map<String, Serializable> feature, Classification classification) {
        this.featureMap = feature;
        this.classification = classification;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }



    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(featureMap);
        out.writeObject(classification);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.featureMap = in.readObject();
        this.classification = in.readObject();
    }

    public Map<String, Serializable> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Serializable> featureMap) {
        this.featureMap = featureMap;
    }
}
