package com.hazelcast.machinelearning.mlcommon;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by berkgokden on 12/21/14.
 */
public class FeatureComparators {
    public static class DoubleEuclideanDistanceFeatureComparator implements IFeatureComparator {
        @Override
        public double compare(Map<String, Serializable> o1, Map<String, Serializable> o2) {
            double sum = 0;
            for (Map.Entry<String, Serializable> entry : o1.entrySet())
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

    public static class DoubleManhattanDistanceFeatureComparator implements IFeatureComparator {
        @Override
        public double compare(Map<String, Serializable> o1, Map<String, Serializable> o2) {
            double sum = 0;
            for (Map.Entry<String, Serializable> entry : o1.entrySet())
            {
                try {
                    Double value1 = (Double) entry.getValue();
                    Double value2 = (Double) o2.get(entry.getKey());
                    sum += Math.abs(value1.doubleValue() - value2.doubleValue());
                } catch (Exception e) {
                    //mostly for casting exception
                }
            }
            return sum;
        }
    }
}
