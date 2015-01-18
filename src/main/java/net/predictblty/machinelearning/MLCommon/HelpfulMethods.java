package net.predictblty.machinelearning.mlcommon;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.util.Base64;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by berkgokden on 12/21/14.
 */
public class HelpfulMethods {

    private HelpfulMethods() {
    }

    /*
    * Build cluster method taken from hazelcast-mapreduce-presentation
    * */
    public static HazelcastInstance buildCluster(int memberCount) {
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

    /*
    * Build cluster method taken from hazelcast-mapreduce-presentation
    * */
    public static HazelcastInstance[] buildClusterReturnCluster(int memberCount) {
        Config config = new Config();
        //NetworkConfig networkConfig = config.getNetworkConfig();
        //networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        //networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        //networkConfig.getJoin().getTcpIpConfig().setMembers(Arrays.asList(new String[]{"127.0.0.1"}));

        HazelcastInstance[] hazelcastInstances = new HazelcastInstance[memberCount];
        for (int i = 0; i < memberCount; i++) {
            hazelcastInstances[i] = Hazelcast.newHazelcastInstance(config);
        }
        return hazelcastInstances;
    }

    public static double compareClassificationsWithClass(Collection<Classification> classifications, String classification) {
        double success = 0;
        for (Classification classification1 : classifications) {
            if (classification1.getClassification().equals(classification)) {
                success = classification1.getConfidence();
                break;
            }
        }
        return success;
    }

    public static ByteHolder generateSortedFetureByteArray(Map<String, Serializable> featureMap) {
        List<String> list = new ArrayList<String>(featureMap.size());

        int requiredSize = 0;
        for (Map.Entry<String, Serializable> stringSerializableEntry : featureMap.entrySet()) {
            list.add(stringSerializableEntry.getValue().toString());
            requiredSize += stringSerializableEntry.getValue().toString().getBytes().length+1;
        }
        Collections.sort(list);

        byte end = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(requiredSize);
        for (String s : list) {
            //byte[] encodedBytes = Base64.encode(s.getBytes());
            byteBuffer.put(s.getBytes());
            byteBuffer.put(end);
        }

        return new ByteHolder(byteBuffer.array());
    }


}
