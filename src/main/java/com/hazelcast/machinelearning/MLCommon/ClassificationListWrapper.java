package com.hazelcast.machinelearning.MLCommon;

import com.hazelcast.machinelearning.methods.impl.Classification;

import java.util.List;

/**
 * Created by berkgokden on 9/21/14.
 */
public class ClassificationListWrapper {
    private List<com.hazelcast.machinelearning.methods.impl.Classification> classificationList;

    public ClassificationListWrapper() {}
    public ClassificationListWrapper(List<com.hazelcast.machinelearning.methods.impl.Classification> classificationList) {
        this.classificationList = classificationList;
    }

    public List<com.hazelcast.machinelearning.methods.impl.Classification> getClassificationList() {
        return classificationList;
    }

    public void setClassificationList(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }
}
