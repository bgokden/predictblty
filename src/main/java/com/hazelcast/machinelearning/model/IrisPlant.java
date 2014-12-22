package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.MLCommon.Classification;
import com.hazelcast.machinelearning.MLCommon.ClassifiedFeature;
import com.hazelcast.machinelearning.MLCommon.Feature;

/**
 * Created by berkgokden on 9/20/14.
 */
public class IrisPlant extends ClassifiedFeature {
    public IrisPlant() {
        super();
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
        this.getFeatureMap().put("sepalLength", sepalLength);
    }

    public double getSepalWidth() {
        return sepalWidth;
    }

    public void setSepalWidth(double sepalWidth) {
        this.sepalWidth = sepalWidth;
        this.getFeatureMap().put("sepalWidth", sepalWidth);
    }

    public double getPetalLength() {
        return petalLength;
    }

    public void setPetalLength(double petalLength) {
        this.petalLength = petalLength;
        this.getFeatureMap().put("petalLength", petalLength);
    }

    public double getPetalWidth() {
        return petalWidth;
    }

    public void setPetalWidth(double petalWidth) {
        this.petalWidth = petalWidth;
        this.getFeatureMap().put("petalWidth", petalWidth);
    }

    public String getPlantClass() {
        return plantClass;
    }

    public void setPlantClass(String plantClass) {
        this.plantClass = plantClass;
        this.classification = new Classification(this.plantClass);
    }
}
