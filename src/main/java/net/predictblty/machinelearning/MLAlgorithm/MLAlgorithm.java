package net.predictblty.machinelearning.MLAlgorithm;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IdGenerator;
import net.predictblty.machinelearning.MLCommon.Classification;
import net.predictblty.machinelearning.MLCommon.Classification;
import net.predictblty.machinelearning.MLCommon.Classification;

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

    //MLAlgorihtm is hazelcast aware but at some point hazelcast instance could be needed before
    //MLAlgotithm is used in a hazelcast distiruted object so giving it in constructer solves a lot.
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

    //implement this method to store training objects into a map or create models
    public abstract void  train(Collection<? extends Object> data) throws Exception;

    //implement this method for prediction algorithm ( probably a map reduce solution)
    public abstract Collection<Classification> predict(Collection<? extends Object> data) throws Exception;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    //A unique id for every mlalgorithm instance used for map id, etc
    public Long getDataId() {
        if (this.dataId == null) {
            IdGenerator idGen = hazelcastInstance.getIdGenerator("dataId");
            this.dataId = idGen.newId();
        }
        return this.dataId;
    }

    //If you want to use any existing datasource you should set DataId before training or predicting
    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    //set Options does not work well after initilization so don't use it
    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
