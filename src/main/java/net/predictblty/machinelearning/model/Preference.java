package net.predictblty.machinelearning.model;

import net.predictblty.machinelearning.annotations.FeatureInfo;

/**
 * Created by berkgokden on 1/12/15.
 */
public class Preference {
    @FeatureInfo
    private String preferenceKey;
    @FeatureInfo
    private String prefenceValue;
    @FeatureInfo(featureType = FeatureInfo.FeatureType.COEFFICIENT)
    private Double prefenceCoefficient;

    public Preference() {
    }

    public Preference(String preferenceKey, String prefenceValue, Double prefenceCoefficient) {
        this.preferenceKey = preferenceKey;
        this.prefenceValue = prefenceValue;
        this.prefenceCoefficient = prefenceCoefficient;
    }

    public Preference(String preferenceKey,Double prefenceCoefficient) {
        this.preferenceKey = preferenceKey;
        this.prefenceCoefficient = prefenceCoefficient;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public void setPreferenceKey(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public String getPrefenceValue() {
        return prefenceValue;
    }

    public void setPrefenceValue(String prefenceValue) {
        this.prefenceValue = prefenceValue;
    }

    public Double getPrefenceCoefficient() {
        return prefenceCoefficient;
    }

    public void setPrefenceCoefficient(Double prefenceCoefficient) {
        this.prefenceCoefficient = prefenceCoefficient;
    }
}
