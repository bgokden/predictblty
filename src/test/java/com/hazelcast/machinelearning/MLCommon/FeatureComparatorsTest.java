package com.hazelcast.machinelearning.mlcommon;

import org.junit.Test;

import static org.junit.Assert.*;

public class FeatureComparatorsTest {

    @Test
    public void shouldPassWhenDistanceCalculatedAsExpected() throws Exception {
        IFeatureComparator doubleFeatureComparator = new FeatureComparators.DoubleEuclideanDistanceFeatureComparator();
        UnclassifiedFeature feature1 = new UnclassifiedFeature();
        ClassifiedFeature feature2 = new ClassifiedFeature();

        feature1.getFeatureMap().put("n1", 1.0D);
        feature1.getFeatureMap().put("n2", 2.0D);

        feature2.getFeatureMap().put("n1", 3.0D);
        feature2.getFeatureMap().put("n2", 4.0D);

        double distance = doubleFeatureComparator.compare(feature1.getFeatureMap(), feature2.getFeatureMap());
        double expectedDistance = Math.sqrt(Math.pow(3.00-1.00,2)+Math.pow(4.00-2.00, 2));

        System.out.println("Distance: "+distance+ " ,Expected Distance: "+expectedDistance);
        assertTrue("Distances should be equal: ", distance==expectedDistance);

    }

}