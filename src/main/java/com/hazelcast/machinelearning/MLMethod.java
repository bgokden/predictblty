package com.hazelcast.machinelearning;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.ClassifiedFeatureDatum;
import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.machinelearning.methods.impl.FeatureConfidenceTuple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by berkgokden on 9/9/14.
 */
public abstract class MLMethod<T,S> implements HazelcastInstanceAware {
    protected transient HazelcastInstance hazelcastInstance;
    private Long dataId;
    protected Map<String, Object> options;

    public MLMethod() {
    }

    public MLMethod(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        this.hazelcastInstance = hazelcastInstance;
        this.options = options;
        if (this.options == null) {
            this.options = new HashMap<String, Object>();
        }
    }

    public abstract void  train(Collection<ClassifiedFeatureDatum<T,S>> data) throws Exception;
    public abstract Collection<Classification<S>> predict(Collection<Feature<T>> data) throws Exception;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public Long getDataId() {
        if (this.dataId == null) {
            IdGenerator idGen = hazelcastInstance.getIdGenerator("dataId");
            this.dataId = idGen.newId();
        }
        return this.dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

//    public static boolean compareClassifications(Classification classification, Collection<Classification> classifications) {
//        for (Classification classification1 : classifications) {
//            if (classification1.getComparableClassification().equals(classification.getComparableClassification())) {
//                return true;
//            }
//            break;
//        }
//        return false;
//    }
}
