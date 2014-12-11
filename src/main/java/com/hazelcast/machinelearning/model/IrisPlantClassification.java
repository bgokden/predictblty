package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by berkgokden on 10/13/14.
 */
public class IrisPlantClassification implements Classification<String>{
    private Map<String, Double> classifications;
    private Double weight;

    public IrisPlantClassification(String classification) {
        classifications = new LinkedHashMap<String, Double>();
        this.weight = 1.0;
        this.classifications.put(classification, 1.0);
    }

    public IrisPlantClassification() {
        this.classifications = new LinkedHashMap<String, Double>();
        this.weight = 1.0;
    }

    @Override
    public void addClassification(String classification, Double confidence) {
        this.classifications.put(classification,confidence);
    }

    @Override
    public Double getConfidenceForClassification(String classification) {
        return null;
    }

    @Override
    public Map<String, Double> getClassifications() {
        return this.classifications;
    }

    @Override
    public void setWeight(Double weight) {

    }

    @Override
    public Double getWeight() {
        return null;
    }

    @Override
    public int size() {
        return this.classifications.size();
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(this.classifications.size());

        for (Map.Entry<String, Double> entry : this.classifications.entrySet()) {
            objectDataOutput.writeUTF(entry.getKey());
            objectDataOutput.writeDouble(entry.getValue());
        }
        objectDataOutput.writeDouble(weight);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        int size = objectDataInput.readInt();
        for (int i = 0; i < size; i++) {
            String classification = objectDataInput.readUTF();
            Double confidence = objectDataInput.readDouble();
            this.classifications.put(classification, confidence);
        }
        this.weight = objectDataInput.readDouble();
    }
}
