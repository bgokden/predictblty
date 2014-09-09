package com.hazelcast.machinelearning.methods.impl;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by berkgokden on 9/9/14.
 */
public class KeyValueTuple implements DataSerializable {
    private String key;
    private double value;

    public KeyValueTuple() {
    }

    public KeyValueTuple(String key, Double value) {
        this.key = key;
        this.value = value.doubleValue();
    }

    public KeyValueTuple(String key, double value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(key);
        objectDataOutput.writeDouble(value);

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        key = objectDataInput.readUTF();
        value = objectDataInput.readDouble();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return this.key+" : "+this.value;
    }
}