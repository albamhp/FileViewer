package me.albamhp.gui.components;

import me.albamhp.VideoManager;
import me.albamhp.utils.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlayButton extends JButton implements VideoComponent {
    
    private VideoManager manager;
    
    public void init(VideoManager manager) {
        this.manager = manager;
        
        addActionListener((ActionEvent action) -> manager.togglePlay());
    }
    
    private ImageIcon playIcon = Icons.getIcon("/play.png");
    private ImageIcon pauseIcon = Icons.getIcon("/pause.png");
    
    public PlayButton() {
        setIcon(true);
    }
    
    // True to set a play button, false for a pause icon
    public void setIcon(boolean play) {
        setIcon(play ? playIcon : pauseIcon);
    }
    
}
