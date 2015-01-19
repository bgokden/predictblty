package net.predictblty.machinelearning.mlalgorithm.mapperimpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import net.predictblty.machinelearning.mlcommon.Classification;
import net.predictblty.machinelearning.mlcommon.IFeatureComparator;
import net.predictblty.machinelearning.mlcommon.Reflections;
import net.predictblty.machinelearning.mlcommon.UnclassifiedFeature;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by berkgokden on 9/16/14.
 */
public class DistanceBasedClassificationAlgorithmMapper implements Mapper<Map<String, Serializable>, Classification, Map<String, Serializable>, Classification>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<UnclassifiedFeature> data;
    private IFeatureComparator comparator;

    public DistanceBasedClassificationAlgorithmMapper(Map<String, Object> options, Collection<? extends Object> data) {
        this.data = new ArrayList<UnclassifiedFeature>(data.size());
        for (Object object : data) {
            this.data.add(Reflections.getUnclassifiedFeatureFromObject(object));
        }
        this.comparator = (IFeatureComparator) options.get("comparator");
    }

    @Override
    public void map(Map<String, Serializable> key, Classification value, Context<Map<String, Serializable>, Classification> context) {
        for (UnclassifiedFeature unclassifiedFeature : this.data) {
            //double distance = unclassifiedFeature.getFeature().distanceTo(key);
            double distance = this.comparator.compare(unclassifiedFeature.getFeatureMap(), key);
            double weight = Double.MAX_VALUE;
            if (distance != 0) {
                weight = value.getConfidence() * unclassifiedFeature.getConfidence() / distance;
            }
            context.emit(unclassifiedFeature.getFeatureMap(), new Classification(value.getClassification(), weight));
            //System.out.println("New map:"+value.getClassification().toString());
        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
