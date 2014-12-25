package net.predictblty.machinelearning;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IdGenerator;
import net.predictblty.machinelearning.methods.impl.Classification;
import net.predictblty.machinelearning.methods.impl.ClassifiedFeatureDatum;
import net.predictblty.machinelearning.methods.impl.FeatureConfidenceTuple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by berkgokden on 9/9/14.
 */
@Deprecated
public abstract class MLMethod implements HazelcastInstanceAware {
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

    public abstract void  train(Collection<ClassifiedFeatureDatum> data) throws Exception;
    public abstract Collection<Classification> predict(Collection<FeatureConfidenceTuple> data) throws Exception;

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
