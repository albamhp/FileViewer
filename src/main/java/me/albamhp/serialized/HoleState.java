package me.albamhp.serialized;

import java.io.Serializable;

public class HoleState implements Serializable {
    public int time;
    public byte state;
    
    @Override
    public String toString() {
        return "HoleState{" +
                "time=" + time +
                ", state=" + state +
                '}';
    }
}
