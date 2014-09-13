package com.hazelcast.machinelearning;

import com.hazelcast.core.HazelcastInstance;

import java.util.Collection;

/**
 * Created by berkgokden on 9/9/14.
 */
public interface MLMethod {
    void train(Collection data) throws Exception;
    Collection predict(Collection data);
}
