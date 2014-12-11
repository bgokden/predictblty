package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.methods.impl.Feature;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

/**
 * Created by berkgokden on 9/20/14.
 */
public class IrisPlantFeature implements Feature<IrisPlantFeature>{
    public IrisPlantFeature() {
    }

    private double confidence;
    private double sepalLength;// in cm
    private double sepalWidth;// in cm
    private double petalLength;// in cm
    private double petalWidth;// in cm
//    private String plantClass;
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

//    public String getPlantClass() {
//        return plantClass;
//    }
//
//    public void setPlantClass(String plantClass) {
//        this.plantClass = plantClass;
//        this.classification = new Classification(this.plantClass);
//    }

    @Override
    public double distanceTo(Feature<IrisPlantFeature> feature) {
        double distance = 0;
        if (feature.getClass().equals(IrisPlantFeature.class)) {
            IrisPlantFeature irisPlantFeature = (IrisPlantFeature) feature;

            distance += Math.pow(this.sepalLength - irisPlantFeature.getSepalLength(), 2);
            distance += Math.pow(this.sepalWidth - irisPlantFeature.getSepalWidth(), 2);
            distance += Math.pow(this.petalLength - irisPlantFeature.getPetalLength(), 2);
            distance += Math.pow(this.petalWidth - irisPlantFeature.getPetalWidth(), 2);
            distance = Math.sqrt(distance); //You can skip this step
        }
        return distance;
    }

    @Override
    public double getConfidence() {
        return this.confidence;
    }

    @Override
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(this.sepalLength);
        out.writeDouble(this.sepalWidth);
        out.writeDouble(this.petalLength);
        out.writeDouble(this.petalWidth);
        out.writeDouble(this.confidence);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.sepalLength = in.readDouble();
        this.sepalWidth = in.readDouble();
        this.petalLength = in.readDouble();
        this.petalWidth = in.readDouble();
        this.confidence = in.readDouble();
    }
}
