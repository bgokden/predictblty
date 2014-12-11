package com.hazelcast.machinelearning.methods.impl;

import com.hazelcast.nio.serialization.DataSerializable;

import java.util.Collection;

/**
 * Created by berkgokden on 9/18/14.
 */
public interface Feature<T> extends DataSerializable{
    public double distanceTo(Feature<T> feature);
    public double getConfidence();
    public void setConfidence(double confidence);
}
