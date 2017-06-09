package me.albamhp.utils;

import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.function.Predicate;

public class FileDropBuilder {

    private static final Color DEFAULT_BORDER_COLOR = new Color(0f, 0f, 1f, 0.25f);
    
    public static class Data {
        private FileValidatorImpl validator = new FileValidatorImpl(new HashSet<>());

        public Data validate(Predicate<File[]> validation) {
            this.validator.validations.add(validation);
            return this;
        }

    }
    
    private Component c;
    private FileDrop.Listener listener;
    private Data data = new Data();
    
    public FileDropBuilder(Component c, FileDrop.Listener listener) {
        this(c, listener, new Data());
    }

    public FileDropBuilder(Component c, FileDrop.Listener listener, Data data) {
        this.c = c;
        this.listener = listener;
        this.data = data;
    }
    
    public FileDrop build() {
        FileValidator fv = data.validator;
        if (!data.validator.hasValidations()) fv = f -> true;
        if (c instanceof JFXPanel) return new SceneFileDrop(((JFXPanel) c).getScene(), listener, fv);
        else return new SwingFileDrop(c, listener, fv);
    }

    public FileDropBuilder validate(Predicate<File[]> validation) {
        data.validate(validation);
        return this;
    }

}
