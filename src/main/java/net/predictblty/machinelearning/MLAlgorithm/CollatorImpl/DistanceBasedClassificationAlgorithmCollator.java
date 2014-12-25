package net.predictblty.machinelearning.MLAlgorithm.CollatorImpl;

import net.predictblty.machinelearning.MLCommon.Classification;
import com.hazelcast.mapreduce.Collator;

import java.io.Serializable;
import java.util.*;

/**
 * Created by berkgokden on 9/19/14.
 */
public class DistanceBasedClassificationAlgorithmCollator implements Collator<Map.Entry<Map<String, Serializable>, List<Classification>>, List<Classification>> {
    Integer limit;
    public DistanceBasedClassificationAlgorithmCollator(Map<String, Object> options) {
        if ((limit = (Integer) options.get("limit")) == null) {
            limit = new Integer(10);
        }
    }

    @Override
    public List<Classification> collate(Iterable<Map.Entry<Map<String, Serializable>, List<Classification>>> values) {
        //System.out.println("Collate :");
        Map<Serializable, Classification> classificationMap = new HashMap<Serializable, Classification>();
        double coefficient = 0;
        Classification temp = null;
        for (Map.Entry<Map<String, Serializable>, List<Classification>> value : values) {
            List<Classification> classifications = value.getValue();
            for (int i = 0; i < classifications.size(); i++) {
                coefficient = classifications.get(i).getConfidence();
                temp = classificationMap.get(classifications.get(i).getClassification());
                if (temp == null) {
                    temp = new Classification(classifications.get(i).getClassification(), coefficient);
                } else {
                    temp.setConfidence(coefficient + temp.getConfidence());
                }
                classificationMap.put(classifications.get(i).getClassification(), temp);
            }

        }
        List<Classification> result = new LinkedList<Classification>(classificationMap.values());
        Collections.sort(result, new Classification.ClassificationComparator());
        result = result.subList(0, Math.min(this.limit, result.size()));
        double sum = 0;
        for (Classification classification : result) {
            sum += classification.getConfidence();
        }
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setConfidence(result.get(i).getConfidence() / sum);
        }
        return result;
    }
}
