package net.predictblty.machinelearning.mlalgorithm.mapperimpl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import com.sun.tools.javac.util.Pair;
import net.predictblty.machinelearning.mlcommon.*;

import java.io.Serializable;
import java.util.*;

/**
 * Created by berkgokden on 1/12/15.
 */
public class UserBasedCollaborativeFilteringRecommendationAlgorithmMapper implements Mapper<ByteHolder, ClassifiedFeature, Map<String, Serializable>, Classification>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;
    private Collection<UnclassifiedFeature> data;
    private IFeatureComparator comparator;

    public UserBasedCollaborativeFilteringRecommendationAlgorithmMapper(Map<String, Object> options, Collection<? extends Object> data) {
        this.data = new ArrayList<UnclassifiedFeature>(data.size());
        for (Object object : data) {
            this.data.add(Reflections.getUnclassifiedFeatureFromObject(object));
        }
        this.comparator = (IFeatureComparator) options.get("comparator");
    }

    @Override
    public void map(ByteHolder key, ClassifiedFeature value, Context<Map<String, Serializable>, Classification> context) {
        for (UnclassifiedFeature unclassifiedFeature : this.data) {
            List<Pair<Map<String, Serializable>, Classification>> list = new LinkedList<Pair<Map<String, Serializable>, Classification>>();
            Double coef = unclassifiedFeature.getConfidence() * value.getClassification().getConfidence();
            boolean similar = false;
            for (Map.Entry<String, Serializable> stringSerializableEntry : unclassifiedFeature.getFeatureMap().entrySet()) {
                for (Map.Entry<String, Serializable> stringSerializableEntryInner : value.getFeatureMap().entrySet()) {
                    if (stringSerializableEntry.getValue().equals(stringSerializableEntryInner.getValue())) {
                        similar = true;
                    } else {
                        Map<String, Serializable> otherKey = new HashMap<String, Serializable>();
                        otherKey.put("key", stringSerializableEntryInner.getValue());
                        list.add(new Pair<Map<String, Serializable>, Classification>(otherKey, new Classification(stringSerializableEntryInner.getValue(), coef)));
                    }
                }
            }

            if (similar) {
                for (Pair<Map<String, Serializable>, Classification> pair : list) {
                    context.emit(pair.fst, pair.snd);
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
