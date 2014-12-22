package com.hazelcast.machinelearning.MLCommon;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by berkgokden on 9/21/14.
 */
public class ClassificationListWrapper implements DataSerializable {
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

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(classificationList.size());
        for (Classification classification : classificationList) {
            classification.writeData(out);
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Classification c = new Classification();
            c.readData(in);
        }
    }
}
