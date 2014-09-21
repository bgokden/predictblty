package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.ClassifiedFeatureDatum;
import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by berkgokden on 9/20/14.
 */
public class IrisPlant extends ClassifiedFeatureDatum implements Feature<IrisPlant>, DataSerializable {
    public IrisPlant() {
    }

    private double sepalLength;// in cm
    private double sepalWidth;// in cm
    private double petalLength;// in cm
    private double petalWidth;// in cm
    private String plantClass;
    // -- Iris Setosa
    // -- Iris Versicolour
    // -- Iris Virginica

    public double getSepalLength() {
        return sepalLength;
    }

    public void setSepalLength(double sepalLength) {
        this.sepalLength = sepalLength;
    }

    public double getSepalWidth() {
        return sepalWidth;
    }

    public void setSepalWidth(double sepalWidth) {
        this.sepalWidth = sepalWidth;
    }

    public double getPetalLength() {
        return petalLength;
    }

    public void setPetalLength(double petalLength) {
        this.petalLength = petalLength;
    }

    public double getPetalWidth() {
        return petalWidth;
    }

    public void setPetalWidth(double petalWidth) {
        this.petalWidth = petalWidth;
    }

    public String getPlantClass() {
        return plantClass;
    }

    public void setPlantClass(String plantClass) {
        this.plantClass = plantClass;
        this.classification = new Classification(this.plantClass);
    }

    @Override
    public Feature getFeature() {
        return this;
    }

    @Override
    public double distanceTo(IrisPlant feature) {
        double distance = 0;
        distance += Math.pow(this.sepalLength - feature.getSepalLength(),2);
        distance += Math.pow(this.sepalWidth - feature.getSepalWidth(),2);
        distance += Math.pow(this.petalLength - feature.getPetalLength(),2);
        distance += Math.pow(this.petalWidth - feature.getPetalWidth(),2);
        distance = Math.sqrt(distance); //You can skip this step
        return distance;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(this.sepalLength);
        out.writeDouble(this.sepalWidth);
        out.writeDouble(this.petalLength);
        out.writeDouble(this.petalWidth);
        out.writeUTF(this.plantClass);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.sepalLength = in.readDouble();
        this.sepalWidth = in.readDouble();
        this.petalLength = in.readDouble();
        this.petalWidth = in.readDouble();
        String pClass = in.readUTF();
        this.setPlantClass(pClass);
    }
}
