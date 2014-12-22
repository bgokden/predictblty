package com.hazelcast.machinelearning.MLCommon;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by berkgokden on 12/21/14.
 */
public interface IFeatureComparator extends Serializable {
    double compare(Feature o1, Feature o2);
    double compare(Map<String, Object> o1, Map<String, Object> o2);
}
