package me.albamhp.utils;

import me.albamhp.Main;

import javax.swing.*;
import java.awt.*;

public class Icons {
    
    private static int WIDTH = 32;
    private static int HEIGHT = 32;
    
    public static ImageIcon getIcon(String path) {
        return new ImageIcon(new ImageIcon(Main.class.getResource(path)).getImage()
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    }
    
}
