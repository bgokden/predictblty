package net.predictblty.machinelearning.mlalgorithm.mapperImpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import net.predictblty.machinelearning.mlcommon.*;

import java.io.Serializable;
import java.util.*;

/**
 * Created by berkgokden on 1/12/15.
 */
public class UserBasedCollaborativeFilteringRecommandationAlgorithmMapper implements Mapper<ByteHolder, ClassifiedFeature, Map<String, Serializable>, Classification>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<UnclassifiedFeature> data;
    private IFeatureComparator comparator;

    public UserBasedCollaborativeFilteringRecommandationAlgorithmMapper(Map<String, Object> options, Collection<? extends Object> data) {
        this.data = new ArrayList<UnclassifiedFeature>(data.size());
        for (Object object : data) {
            this.data.add(Reflections.getUnclassifiedFeatureFromObject(object));
        }
        this.comparator = (IFeatureComparator) options.get("comparator");
    }

    @Override
    public void map(ByteHolder key, ClassifiedFeature value, Context<Map<String, Serializable>, Classification> context) {
        for (UnclassifiedFeature unclassifiedFeature : this.data) {
            //double distance = unclassifiedFeature.getFeature().distanceTo(key);
//            double distance = this.comparator.compare(unclassifiedFeature.getFeatureMap(), key);
//            double weight = Double.MAX_VALUE;
//            if (distance != 0) {
//                weight = value.getConfidence() * unclassifiedFeature.getConfidence() / distance;
//            }
            List<Classification> list = new LinkedList<Classification>();
            Double coef = unclassifiedFeature.getConfidence() * value.getClassification().getConfidence();
            boolean similar = false;
            for (Map.Entry<String, Serializable> stringSerializableEntry : unclassifiedFeature.getFeatureMap().entrySet()) {
                for (Map.Entry<String, Serializable> stringSerializableEntryInner : value.getFeatureMap().entrySet()) {
                    if (stringSerializableEntry.getValue().toString().equals(stringSerializableEntryInner.getValue().toString())) {
                        similar = true;
                    } else {
                        list.add(new Classification(stringSerializableEntry.getValue(), coef));
                    }
                }
            }

            if (similar) {
                for (Classification classification : list) {
                    context.emit(unclassifiedFeature.getFeatureMap(), classification);
                }
            }
            //System.out.println("New map:"+value.getClassification().toString());
        }

    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
