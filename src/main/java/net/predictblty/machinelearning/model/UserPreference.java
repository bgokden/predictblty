package net.predictblty.machinelearning.model;

import net.predictblty.machinelearning.annotations.FeatureInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by berkgokden on 1/12/15.
 */
public class UserPreference {
    private String userId;
    private Map<String, Double> prefences;

    public UserPreference() {
    }

    public UserPreference(String userId, Map<String, Double> prefences) {
        this.userId = userId;
        this.prefences = prefences;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Double> getPrefences() {
        return prefences;
    }

    public void setPrefences(Map<String, Double> prefences) {
        this.prefences = prefences;
    }

    public void addPreference(String key, Double value) {
        if (this.prefences == null) {
            this.prefences = new HashMap<String, Double>();
        }
        Double currentValue = this.prefences.get(key);
        if (currentValue == null) {
            currentValue = new Double(0);
        }
        currentValue += value;
        this.prefences.put(key, value);
    }
}
