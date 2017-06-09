package me.albamhp.gui.components;

import javax.swing.*;

public class Splitter extends JSplitPane {
    
    public Splitter() {
        SwingUtilities.invokeLater(() -> setDividerLocation(0.5));
    }
    
}
