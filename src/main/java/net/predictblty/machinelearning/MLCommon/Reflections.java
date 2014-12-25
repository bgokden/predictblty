package net.predictblty.machinelearning.MLCommon;

import net.predictblty.machinelearning.annotations.FeatureInfo;
import net.predictblty.machinelearning.annotations.FeatureInfo;
import net.predictblty.machinelearning.annotations.FeatureInfo;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by berkgokden on 12/22/14.
 */
public class Reflections {

    private Reflections() {
    }

    public static ClassifiedFeature getClassifiedFeatureFromObject(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof ClassifiedFeature) {
            return ((ClassifiedFeature) obj);
        }

        Class objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        ClassifiedFeature classifiedFeature = new ClassifiedFeature();
        for (int i = 0; i < fields.length; i++) {
            try {
                FeatureInfo featureInfo = fields[i].getAnnotation(FeatureInfo.class);
                if (featureInfo != null) {
                    fields[i].setAccessible(true);
                    if (featureInfo.featureType().equals(FeatureInfo.FeatureType.FEATURE)) {
                        String name = null;
                        if (featureInfo.name() != null && !featureInfo.name().trim().isEmpty()) {//not empty
                            name = featureInfo.name();
                        } else {
                            name = fields[i].getName();
                        }
                        Serializable value = (Serializable) fields[i].get(obj);
                        classifiedFeature.getFeatureMap().put(name, value);
                    } else if (featureInfo.featureType().equals(FeatureInfo.FeatureType.CLASSIFICATION)) {
                        Serializable value = (Serializable) fields[i].get(obj);
                        classifiedFeature.getClassification().setClassification(value);
                    } else if (featureInfo.featureType().equals(FeatureInfo.FeatureType.COEFFICIENT)) {
                        double value = (Double) fields[i].get(obj);
                        classifiedFeature.getClassification().setConfidence(value);
                    }
                }
            } catch (Exception e) {
            }
        }

        return classifiedFeature;
    }


    public static UnclassifiedFeature getUnclassifiedFeatureFromObject(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof UnclassifiedFeature) {
            return ((UnclassifiedFeature) obj);
        }

        Class objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        UnclassifiedFeature unclassifiedFeature = new UnclassifiedFeature();
        for (int i = 0; i < fields.length; i++) {
            try {
                FeatureInfo featureInfo = fields[i].getAnnotation(FeatureInfo.class);
                if (featureInfo != null) {
                    fields[i].setAccessible(true);
                    if (featureInfo.featureType().equals(FeatureInfo.FeatureType.FEATURE)) {
                        String name = null;
                        if (featureInfo.name() != null && !featureInfo.name().trim().isEmpty()) {//not empty
                            name = featureInfo.name();
                        } else {
                            name = fields[i].getName();
                        }
                        Serializable value = (Serializable) fields[i].get(obj);
                        unclassifiedFeature.getFeatureMap().put(name, value);
                    } else if (featureInfo.featureType().equals(FeatureInfo.FeatureType.COEFFICIENT)) {
                        double value = (Double) fields[i].get(obj);
                        unclassifiedFeature.setConfidence(value);
                    }
                }
            } catch (Exception e) {
            }
        }

        return unclassifiedFeature;
    }
}
