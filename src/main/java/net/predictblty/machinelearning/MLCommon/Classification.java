package net.predictblty.machinelearning.mlcommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by berkgokden on 12/17/14.
 */
public class Classification implements DataSerializable {
    private double confidence;
    private Serializable classification;//I wanted to define it as an object but serialization could have issues with it

    public Classification(Serializable classification, double confidence) {
        this.classification = classification;
        this.confidence = confidence;
    }

    public Classification(Serializable classification, Double confidence) {
        this.classification = classification;
        if (confidence == null) {
            this.confidence = 1.0;
        } else {
            this.confidence = confidence.doubleValue();
        }
    }

    public Classification(String classification) {
        this.classification = classification;
        this.confidence = 1.0;
    }

    public Classification() {
        this.confidence = 1.0;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Serializable getClassification() {
        return classification;
    }

    public void setClassification(Serializable classification) {
        this.classification = classification;
    }

    public String toString() {
        return this.classification.toString()+" "+this.confidence;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(this.classification);
        out.writeDouble(this.confidence);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.classification = in.readObject();
        this.confidence = in.readDouble();
    }

    public static class ClassificationComparator
            implements Comparator<Classification> {

        @Override
        public int compare(Classification o1, Classification o2) {
            return Double.compare(o2.getConfidence(), o1.getConfidence());
        }
    }
}
