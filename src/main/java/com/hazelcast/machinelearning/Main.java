package com.hazelcast.machinelearning;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.hazelcast.machinelearning.methods.SparseMatrixMultiplication;
import com.hazelcast.machinelearning.methods.impl.KeyValueTuple;

import java.util.Arrays;

/**
 * Created by berkgokden on 9/9/14.
 */
public class Main {

    public static void main(String[] args)
            throws Exception {

        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = buildCluster(2);

        // Read CSV data
        //ReaderHelper.read(hazelcastInstance);

        MultiMap<String, KeyValueTuple> multiMap = hazelcastInstance.getMultiMap("keyValueTuples");
        multiMap.put("EventA", new KeyValueTuple("ResultB", Math.random() * 99 + 1));
        multiMap.put("EventA", new KeyValueTuple("ResultC", Math.random() * 99 + 1));

        multiMap.put("EventB", new KeyValueTuple("ResultA", Math.random() * 99 + 1));
        multiMap.put("EventB", new KeyValueTuple("ResultC", Math.random() * 99 + 1));

        multiMap.put("EventC", new KeyValueTuple("ResultA", Math.random() * 99 + 1));
        multiMap.put("EventC", new KeyValueTuple("ResultB", Math.random() * 99 + 1));

        try {
            // Execute Tutorial
            // Tutorial tutorial = new Tutorial1();
            // Tutorial tutorial = new Tutorial2();
            // Tutorial tutorial = new Tutorial3();
            // Tutorial tutorial = new Tutorial4();
            // Tutorial tutorial = new Tutorial5();
            // Tutorial tutorial = new Tutorial6();
            // Tutorial tutorial = new Tutorial7();
            SparseMatrixMultiplication method = new SparseMatrixMultiplication();
            method.execute(hazelcastInstance);

        } finally {
            // Shutdown cluster
            Hazelcast.shutdownAll();
        }
    }


    /*
    * Build cluster method taken from hazelcast-mapreduce-presentation
    * */
    private static HazelcastInstance buildCluster(int memberCount) {
        Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.getJoin().getTcpIpConfig().setMembers(Arrays.asList(new String[]{"127.0.0.1"}));

        HazelcastInstance[] hazelcastInstances = new HazelcastInstance[memberCount];
        for (int i = 0; i < memberCount; i++) {
            hazelcastInstances[i] = Hazelcast.newHazelcastInstance(config);
        }
        return hazelcastInstances[0];
    }
}
