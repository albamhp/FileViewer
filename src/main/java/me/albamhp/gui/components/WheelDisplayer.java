package me.albamhp.gui.components;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.albamhp.VideoManager;
import me.albamhp.serialized.Hole;
import me.albamhp.serialized.SerializedData;
import me.albamhp.utils.FileDropBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WheelDisplayer extends JComponent implements VideoComponent {
    private int ROWS = 0;
    private int COLS = 0;
    
    private static final int BORDER = 5;
    
    private SerializedData data = null;
    private List<HoleManager> managers = new ArrayList<>();
    
    private VideoManager manager;
    
    public void init(VideoManager manager) {
        this.manager = manager;
    }
    
    public WheelDisplayer() {
        //setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0, 0, 0)));
        new FileDropBuilder(this, (File[] files) -> {
            try {
                data = new Gson().fromJson(new FileReader(files[0]), SerializedData.class);
                rewriteLayout();
            } catch (JsonSyntaxException e) {
                JOptionPane.showMessageDialog(this, "Invalid json file");
            } catch (FileNotFoundException e) {
                // TODO: better exception handling, tell the user something went wrong
                throw new RuntimeException(e);
            }
        }).validate((File[] files) -> files.length == 1).build();
    }
    
    private void rewriteLayout() {
        if (data == null) throw new IllegalStateException("Can't rewrite layout without having data first!");
        this.removeAll();
        ROWS = data.rows;
        COLS = data.cols;
        this.setLayout(new GridLayout(ROWS, COLS));
        int cells = ROWS * COLS;
        for (Hole hole : data.holes) {
            if (--cells < 0) break;
            HoleManager manager = new HoleManager(hole);
            this.add(manager);
            managers.add(manager);
        }
        manager.update();
        this.revalidate();
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (data != null) {
            // If we have data, we know rows and columns, and proportion needed, so calculate it
            int oldW = width, oldH = height;
            double size = Math.min(width * ROWS, height * COLS);
            width = (int) (size / ROWS);
            height = (int) (size / COLS);
            x = x - ((width - oldW) / 2);
            y = y - ((height - oldH) / 2);
        }
        super.setBounds(addPercent(x, BORDER), addPercent(y, BORDER),
                addPercent(width, -BORDER * 2), addPercent(height, -BORDER * 2));
    }
    
    private static int addPercent(int val, int percent) {
        return val * (100 + percent) / 100;
    }
    
    public void updateTo(int millis) {
        this.managers.parallelStream().forEach(m -> m.updateTo(millis));
        repaint();
    }

}
