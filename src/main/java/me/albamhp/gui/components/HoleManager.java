package me.albamhp.gui.components;

import me.albamhp.serialized.Hole;
import me.albamhp.serialized.HoleState;

import javax.swing.*;
import java.awt.*;

public class HoleManager extends JComponent {
    private static final byte EMPTY = 0b00;
    private static final byte ERROR = 0b01;
    private static final byte SMALL = 0b10;
    private static final byte LARGE = 0b11;
    
    private static final Color SMALL_COLOR = Color.BLUE;
    private static final Color LARGE_COLOR = Color.GREEN;
    private static final Color ERROR_COLOR = Color.RED;
    
    // All of therese are in percentage, 20 means a 20% of the width/height, all of them are diameters
    private static final int CENTER_SIZE = 20;
    private static final int SMALL_GEAR = 70;
    private static final int LARGE_GEAR = 95;
    private static final int ERROR_SIZE = 80;
    
    private HoleState[] states;
    private HoleState currState;
    private int currIndex;
    
    public HoleManager(Hole hole) {
        this.setDoubleBuffered(true);
        
        this.states = hole.states;
        setPosition(0);
    }
    
    private int size(int percent) {
        return (int) ((double) (getWidth() * percent) / 100);
    }
    
    private int position(int size) {
        return (getWidth() - size) / 2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if (currState != null && currState.state != EMPTY) {
            if      (currState.state == SMALL) drawCircle(g, SMALL_GEAR, SMALL_COLOR);
            else if (currState.state == LARGE) drawCircle(g, LARGE_GEAR, LARGE_COLOR);
            else if (currState.state == ERROR) drawRect(g, ERROR_SIZE, ERROR_COLOR);
        }
        drawCircle(g, CENTER_SIZE, Color.BLACK);
    }
    
    private void drawCircle(Graphics g, int sizePercent, Color color) {
        g.setColor(color);
        int size = size(sizePercent);
        drawCircle(g, position(size), size);
    }
    
    private void drawCircle(Graphics g, int pos, int size) {
        g.fillOval(pos, pos, size, size);
    }
    
    private void drawRect(Graphics g, int sizePercent, Color color) {
        g.setColor(color);
        int size = size(sizePercent);
        drawRect(g, position(size), size);
    }
    
    private void drawRect(Graphics g, int pos, int size) {
        g.fillRect(pos, pos, size, size);
    }
    
    private void setPosition(int index) {
        this.currState = states[index];
        this.currIndex = index;
    }
    
    public void updateTo(int millis) {
        setPosition(getNextIndex(millis));
    }
    
    private int getNextIndex(int millis) {
        // Only needs moving foward (or no change)
        if (currState.time <= millis) {
            // Can't move foward, end of elements
            if (!existsIndex(currIndex + 1)) return currIndex;

            int nextTime = states[currIndex + 1].time;
            // Current is already ok
            if (nextTime > millis) return currIndex;
            // Try directly next one
            else if (nextTime <= millis && (!existsIndex(currIndex + 2) || states[currIndex + 2].time > millis))
                return currIndex + 1;
            // Anything else above it
            else return findState(millis, currIndex + 1, states.length - 1);
        }
        // Find it backwards
        return findState(millis, 0, currIndex);
    }
    
    private int findState(int millis, int lb, int ub) {
        final int mid = (lb + ub) >> 1;
        
        // Too low, return first state
        if (mid == lb && states[mid].time > millis) return 0;
        
        // Found a candidate, and can't go higher
        if (states[mid].time <= millis && (mid == ub || states[mid + 1].time > millis)) return mid;
        
        if (states[mid].time <= millis) return findState(millis, mid + 1, ub);
        else return findState(millis, lb, mid);
    }

    private boolean existsIndex(int index) {
        return index < states.length;
    }
    
}

