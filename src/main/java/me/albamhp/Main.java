package me.albamhp;

import me.albamhp.gui.GUI;

import javax.swing.*;

public class Main {
    
    private static GUI gui;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException
                | UnsupportedLookAndFeelException | InstantiationException ex) {
            ex.printStackTrace();
        }
    
        SwingUtilities.invokeLater(() -> gui = new GUI());
    }
    
}
