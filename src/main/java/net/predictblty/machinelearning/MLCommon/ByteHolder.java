package net.predictblty.machinelearning.mlcommon;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by berkgokden on 1/15/15.
 */
public class ByteHolder implements DataSerializable, Serializable {
    private byte[] bytes;

    public ByteHolder() {
    }

    public ByteHolder(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeByteArray(this.bytes);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.bytes = in.readByteArray();
    }
}
