package me.albamhp.serialized;

import java.io.Serializable;
import java.util.Arrays;

public class SerializedData implements Serializable {
    public long initialTime;
    public int rows;
    public int cols;
    public Hole[] holes;
    
    @Override
    public String toString() {
        return "SerializedData{" +
                "startTime=" + initialTime +
                ", holes=" + Arrays.toString(holes) +
                '}';
    }
}
