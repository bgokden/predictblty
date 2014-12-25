package net.predictblty.machinelearning.methods;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import net.predictblty.machinelearning.MLMethod;
import com.hazelcast.mapreduce.*;
import net.predictblty.machinelearning.MLMethod;
import net.predictblty.machinelearning.methods.impl.Classification;
import net.predictblty.machinelearning.methods.impl.ClassifiedFeatureDatum;
import net.predictblty.machinelearning.methods.impl.Feature;
import net.predictblty.machinelearning.methods.impl.FeatureConfidenceTuple;
import net.predictblty.machinelearning.MLMethod;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by berkgokden on 9/13/14.
 */
public class RecommendationMethod extends MLMethod {


    private transient HazelcastInstance hazelcastInstance;
    private MultiMap<Feature, FeatureConfidenceTuple> multiMap;
    private String randomKey;

    public RecommendationMethod(HazelcastInstance hazelcastInstance) {
        super(hazelcastInstance, null);
        this.randomKey = RandomStringUtils.random(20);

        this.multiMap = hazelcastInstance.getMultiMap("traindata-"+this.randomKey);
    }

    //I am not ok with this method
    //I could have used a map here using Predicates but it will reduce prediction performance
    //and prediction performance is much more impartant than training performance
    private void addToMultiMapWithCheckIfExists(ClassifiedFeatureDatum classifiedFeatureDatum) {
        Feature feature =  classifiedFeatureDatum.getFeature();
        boolean foundAndSet = false;
        this.multiMap.lock(feature);
        Collection<FeatureConfidenceTuple>  featureConfidenceTuples = this.multiMap.get(feature);
        for (FeatureConfidenceTuple featureConfidenceTuple : featureConfidenceTuples) {
            if (featureConfidenceTuple.getFeature().equals(classifiedFeatureDatum.getClassification().getComparableClassification()) ) {
                double confidenceCoefficient = classifiedFeatureDatum.getClassification().getConfidenceCoefficient() + featureConfidenceTuple.getConfidenceCoefficient();
                this.multiMap.remove(feature, featureConfidenceTuple);
                foundAndSet = this.multiMap.put(feature, new FeatureConfidenceTuple(classifiedFeatureDatum.getClassification().getComparableClassification(), confidenceCoefficient));
                break;
            }
        }
        if (!foundAndSet) {
            this.multiMap.put(classifiedFeatureDatum.getFeature(), new FeatureConfidenceTuple(classifiedFeatureDatum.getClassification().getComparableClassification(), classifiedFeatureDatum.getClassification().getConfidenceCoefficient()));
        }
        this.multiMap.unlock(feature);
    }

    @Override
    public void train(Collection<ClassifiedFeatureDatum> data) throws Exception {
        for (ClassifiedFeatureDatum classifiedFeatureDatum : data) {
            this.addToMultiMapWithCheckIfExists(classifiedFeatureDatum);
            this.multiMap.put(classifiedFeatureDatum.getFeature(), new FeatureConfidenceTuple(classifiedFeatureDatum.getClassification().getComparableClassification(), classifiedFeatureDatum.getClassification().getConfidenceCoefficient()));
        }
    }

    @Override
    public Collection<Classification> predict(Collection<FeatureConfidenceTuple> data) throws Exception {
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");
        IMap<Feature, Double> map = hazelcastInstance.getMap("predictdata-"+this.randomKey);

        for (FeatureConfidenceTuple featureConfidenceTuple : data) {
            map.put(featureConfidenceTuple.getFeature(), new Double(featureConfidenceTuple.getConfidenceCoefficient()));
        }


        KeyValueSource<Feature, Double> source = KeyValueSource.fromMap(map);

        Job<Feature, Double> job = jobTracker.newJob(source);
//TODO
//        JobCompletableFuture<List<Classification>> future = job //
//                .mapper(new RecommendationMethodMapper(this.randomKey)) //
//                .combiner(new RecommendationMethodCombinerFactory()) //
//                .reducer(new RecommendationMethodReducerFactory()) //
//                .submit(new RecommendationMethodCollator());

        //System.out.println("Result: " + ToStringPrettyfier.toString(future.get()));
        return null;//future.get();
    }

}
