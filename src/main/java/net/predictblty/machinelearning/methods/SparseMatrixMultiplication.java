package net.predictblty.machinelearning.methods;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import net.predictblty.machinelearning.methods.impl.*;

import java.util.List;

/**
 * Created by berkgokden on 9/9/14.
 */
public class SparseMatrixMultiplication {
    //@Override
    public void execute(HazelcastInstance hazelcastInstance)
            throws Exception {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");
        IMap<String, Double> map = hazelcastInstance.getMap("inputs");

        map.put("EventA", new Double(Math.random() * 99 + 1));
        map.put("EventB", new Double(Math.random() * 99 + 1));

        KeyValueSource<String, Double> source = KeyValueSource.fromMap(map);

        Job<String, Double> job = jobTracker.newJob(source);

        JobCompletableFuture<List<KeyValueTuple>> future = job //
                .mapper(new InvertedMapper()) //
                .combiner(new InvertedCombinerFactory()) //
                .reducer(new InvertedReducerFactory()) //
                .submit(new InvertedCollator());

        System.out.println("Result: " + ToStringPrettyfier.toString(future.get()));
    }
}
