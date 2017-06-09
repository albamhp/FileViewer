package me.albamhp.utils;

import java.io.File;

public interface FileDrop {
    
    public interface Listener {
        void filesDropped(File[] files);
    }
    
}
