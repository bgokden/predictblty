package com.hazelcast.machinelearning.MLCommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by berkgokden on 12/21/14.
 */
public class Feature implements DataSerializable, Serializable {
    private static final long serialVersionUID = 1L;
    protected ConcurrentHashMap<String, Serializable> featureMap;

    public Feature() {
    }

    public Feature(Feature feature) { //copy constructer
        for (Map.Entry<String, Serializable> entry : feature.getFeatureMap().entrySet()) {
            this.add(entry.getKey(), entry.getValue());
        }
    }

    public void add(String key, Serializable value) {
        if (this.featureMap == null) {
            this.featureMap = new ConcurrentHashMap<String, Serializable>();
        }
        this.featureMap.put(key, value);
    }

    public Object get(String key) {
        if (this.featureMap == null) {
            return null;
        }
        return this.featureMap.get(key);
    }

    public ConcurrentHashMap<String, Serializable> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(ConcurrentHashMap<String, Serializable> featureMap) {
        this.featureMap = featureMap;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        if (this.featureMap != null) {
            out.writeInt(this.featureMap.size());
            for (Map.Entry<String, Serializable> entry : this.featureMap.entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeObject(entry.getValue());
            }
        } else {
            out.writeInt(0);
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readUTF();
            Serializable value = in.readObject();
            this.add(key, value);
        }
    }
}
