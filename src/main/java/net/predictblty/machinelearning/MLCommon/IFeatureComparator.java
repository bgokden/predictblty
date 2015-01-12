package net.predictblty.machinelearning.mlcommon;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by berkgokden on 12/21/14.
 */
public interface IFeatureComparator extends Serializable {
    double compare(Map<String, Serializable> o1, Map<String, Serializable> o2);
}
