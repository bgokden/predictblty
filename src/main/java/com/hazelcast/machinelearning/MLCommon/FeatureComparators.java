package com.hazelcast.machinelearning.MLCommon;

import java.util.Map;

/**
 * Created by berkgokden on 12/21/14.
 */
public class FeatureComparators {
    public static class DoubleFeatureComparator implements IFeatureComparator {
        @Override
        public double compare(Feature o1, Feature o2) {
            return this.compare(o1.getFeatureMap(), o2.getFeatureMap());
        }

        @Override
        public double compare(Map<String, Object> o1, Map<String, Object> o2) {
            double sum = 0;
            for (Map.Entry<String, Object> entry : o1.entrySet())
            {
                try {
                    Double value1 = (Double) entry.getValue();
                    Double value2 = (Double) o2.get(entry.getKey());
                    sum += Math.pow(value1.doubleValue() - value2.doubleValue(), 2);
                } catch (Exception e) {
                    //mostly for casting exception
                }
            }
            return Math.sqrt(sum);
        }
    }
}
