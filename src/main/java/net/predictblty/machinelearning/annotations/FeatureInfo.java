package net.predictblty.machinelearning.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by berkgokden on 12/22/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FeatureInfo {
    public enum FeatureType {
        FEATURE, CLASSIFICATION, COEFFICIENT
    }
    FeatureType featureType() default FeatureType.FEATURE;
    String name() default "";
    double value() default 1.0D;
}
