package me.albamhp.utils;

import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.List;

public class SceneFileDrop implements FileDrop {
    
    public SceneFileDrop(Scene scene, final FileDrop.Listener listener, FileValidator validator) {
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            } else {
                event.consume();
            }
        });
        
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            File[] files = toFileArray(db.getFiles());
            boolean success = db.hasFiles() && validator.isDragOk(files);
            if (success) listener.filesDropped(files);
            event.setDropCompleted(success);
            event.consume();
        });
        
    }
    
    private static File[] toFileArray(List<File> files) {
        return files.toArray(new File[files.size()]);
    }
    
}
