package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by berkgokden on 9/22/14.
 */
public class StringFeature extends Feature<String> implements Comparable<StringFeature> {

    private String data;
    public StringFeature() {}

    public StringFeature(String data) {
        this.data = data;
    }

    @Override
    public int compareTo(StringFeature o) {
        return this.data.compareTo(o.getData());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(this.data);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.data = in.readUTF();
    }

    @Override
    public double distanceTo(String feature) {
        return ((double) this.data.compareTo(feature));
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
