package com.hazelcast.machinelearning.model;

import com.hazelcast.machinelearning.annotations.FeatureInfo;
import com.hazelcast.machinelearning.annotations.FeatureInfo.FeatureType;

/**
 * Created by berkgokden on 9/20/14.
 */
public class IrisPlant {

    @FeatureInfo
    private double sepalLength;// in cm
    @FeatureInfo
    private double sepalWidth;// in cm
    @FeatureInfo
    private double petalLength;// in cm
    @FeatureInfo
    private double petalWidth;// in cm
    @FeatureInfo(featureType = FeatureType.CLASSIFICATION)
    private String plantClass;
    // -- Iris Setosa
    // -- Iris Versicolour
    // -- Iris Virginica

    public IrisPlant() {
    }

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
    }
}
