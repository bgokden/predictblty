package com.hazelcast.machinelearning.MLCommon;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnclassifiedFeatureTest {

    @Test
    public void shouldPassWhenSerialized() throws Exception {
        try {
            HazelcastInstance[] hz = HelpfulMethods.buildClusterReturnCluster(2);

            Map<String, UnclassifiedFeature> map = hz[0].getMap("map");
            ConcurrentHashMap<String, Serializable> f = new ConcurrentHashMap<String, Serializable>();
            f.put("n1", 1.0D);
            f.put("n2", 2.0D);
            f.put("n3", 3.0D);
            f.put("n4", 4.0D);
            UnclassifiedFeature uf = new UnclassifiedFeature(f);
            map.put("UF", new UnclassifiedFeature());

            Map<String, UnclassifiedFeature> map2 = hz[1].getMap("map");

            UnclassifiedFeature p = map2.get("UF");
            System.out.println(p);
        } finally {
            Hazelcast.shutdownAll();
        }
    }
}