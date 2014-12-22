package com.hazelcast.machinelearning.MLCommon;

import org.junit.Test;

import static org.junit.Assert.*;

public class FeatureComparatorsTest {

    @Test
    public void shouldPassWhenDistanceCalculatedAsExpected() throws Exception {
        IFeatureComparator doubleFeatureComparator = new FeatureComparators.DoubleFeatureComparator();
        Feature feature1 = new Feature();
        Feature feature2 = new Feature();

        feature1.add("n1", 1.0D);
        feature1.add("n2", 2.0D);

        feature2.add("n1", 3.0D);
        feature2.add("n2", 4.0D);

        double distance = doubleFeatureComparator.compare(feature1, feature2);
        double expectedDistance = Math.sqrt(Math.pow(3.00-1.00,2)+Math.pow(4.00-2.00, 2));

        System.out.println("Distance: "+distance+ " ,Expected Distance: "+expectedDistance);
        assertTrue("Distances should be equal: ", distance==expectedDistance);

    }

}