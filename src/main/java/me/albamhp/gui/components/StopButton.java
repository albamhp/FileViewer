package me.albamhp.gui.components;

import me.albamhp.VideoManager;
import me.albamhp.utils.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StopButton extends JButton implements VideoComponent {
    
    private VideoManager manager;
    
    public void init(VideoManager manager) {
        this.manager = manager;
    
        addActionListener((ActionEvent action) -> manager.stop());
    }
    
    public StopButton() {
        setIcon(Icons.getIcon("/stop.png"));
    }
    
}
