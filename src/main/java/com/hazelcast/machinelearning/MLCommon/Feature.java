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
    protected Map<String, Object> featureMap;

    public Feature() {
    }

    public void add(String key, Object value) {
        if (this.featureMap == null) {
            this.featureMap = new ConcurrentHashMap<String, Object>();
        }
        this.featureMap.put(key, value);
    }

    public Object get(String key) {
        if (this.featureMap == null) {
            return null;
        }
        return this.featureMap.get(key);
    }

    public Map<String, Object> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Object> featureMap) {
        this.featureMap = featureMap;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        if (this.featureMap != null) {
            out.writeInt(this.featureMap.size());
            for (Map.Entry<String, Object> entry : this.featureMap.entrySet()) {
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
            Object value = in.readObject();
            this.add(key, value);
        }
    }
}
