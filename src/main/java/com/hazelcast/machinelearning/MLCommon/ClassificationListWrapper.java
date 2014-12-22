package com.hazelcast.machinelearning.MLCommon;


import java.io.Serializable;
import java.util.List;

/**
 * Created by berkgokden on 9/21/14.
 */
public class ClassificationListWrapper implements Serializable {
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
