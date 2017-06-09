package me.albamhp.gui.components;

import me.albamhp.VideoManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimeLine extends JSlider implements VideoComponent {
    
    private VideoManager manager;
    
    public void init(VideoManager manager) {
        this.manager = manager;
        
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                manager.setVideoAt(((BasicSliderUI) getUI()).valueForXPosition(e.getX()));
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                manager.setVideoAt(getValue());
            }
        });
    }
    
    public TimeLine() {
    }
    
}
