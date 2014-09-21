package com.hazelcast.machinelearning.methods.impl;

import java.util.List;

/**
 * Created by berkgokden on 9/21/14.
 */
public class ClassificationListWrapper {
    private List<Classification> classificationList;

    public ClassificationListWrapper() {}
    public ClassificationListWrapper(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }

    public List<Classification> getClassificationList() {
        return classificationList;
    }

    public void setClassificationList(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }
}
