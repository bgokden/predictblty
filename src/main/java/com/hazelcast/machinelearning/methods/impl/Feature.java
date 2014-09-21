package com.hazelcast.machinelearning.methods.impl;

import java.util.Collection;

/**
 * Created by berkgokden on 9/18/14.
 */
public interface Feature<T> {
    public double distanceTo(T feature);
}
