package net.predictblty.machinelearning.methods.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Collection;

/**
 * Created by berkgokden on 9/9/14.
 */
public class InvertedMapper implements Mapper<String, Double, String, Double>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(String key, Double value, Context<String, Double> context) {
        MultiMap<String, KeyValueTuple> multiMap = hazelcastInstance.getMultiMap("keyValueTuples");

        Collection<KeyValueTuple> values = multiMap.get(key);

        for (KeyValueTuple keyValueTuple : values) {
            context.emit(keyValueTuple.getKey(), (keyValueTuple.getValue()*value.doubleValue()) );
        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}