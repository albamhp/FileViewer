package me.albamhp.serialized;

import java.io.Serializable;
import java.util.Arrays;

public class Hole implements Serializable {
    public HoleState[] states;
    
    @Override
    public String toString() {
        return "Hole{" +
                "states=" + Arrays.toString(states) +
                '}';
    }
}
