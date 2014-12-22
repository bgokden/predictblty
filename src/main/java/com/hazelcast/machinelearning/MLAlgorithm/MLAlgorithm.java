package com.hazelcast.machinelearning.MLAlgorithm;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.machinelearning.MLCommon.Classification;
import com.hazelcast.machinelearning.MLCommon.ClassifiedFeature;
import com.hazelcast.machinelearning.MLCommon.UnclassifiedFeature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by berkgokden on 12/16/14.
 */
public abstract class MLAlgorithm implements HazelcastInstanceAware {
    protected transient HazelcastInstance hazelcastInstance;
    private Long dataId;
    protected Map<String, Object> options;

    public MLAlgorithm() {
    }

    public MLAlgorithm(HazelcastInstance hazelcastInstance, Map<String, Object> options) {
        this.hazelcastInstance = hazelcastInstance;
        this.options = options;
        if (this.options == null) {
            this.options = new HashMap<String, Object>();
        }
    }

    public MLAlgorithm(Map<String, Object> options) {
        this.options = options;
        if (this.options == null) {
            this.options = new HashMap<String, Object>();
        }
    }

    public abstract void  train(Collection<ClassifiedFeature> data) throws Exception;
    public abstract Collection<Classification> predict(Collection<UnclassifiedFeature> data) throws Exception;

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
}
