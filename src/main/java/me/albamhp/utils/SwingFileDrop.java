package me.albamhp.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TooManyListenersException;


public class SwingFileDrop implements FileDrop {

    private static final boolean supportsDnD;
    static {
        boolean dnd = false;
        try {
            Class arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
            dnd = true;
        } catch (Exception e) {}
        supportsDnD = dnd;
    }
    
    private final FileDrop.Listener listener;
    private final FileValidator validator;
    
    public SwingFileDrop(Component comp, FileDrop.Listener listener, FileValidator validator) {
        if (comp == null || listener == null || validator == null)
            throw new UnsupportedOperationException("Component, Listener and validator may not be null!");
        this.listener = listener;
        this.validator = validator;
        
        if (!supportsDnD) return; // Everything above is final, so we need to declare it regardless
        
        DropTarget target = new DropTarget();
        comp.setDropTarget(target);
        try {
            target.addDropTargetListener(
                    new DropTargetAdapter() {
                        public void dragEnter(DropTargetDragEvent evt) {fileAction(evt);}
                        public void drop(DropTargetDropEvent evt) {fileDrop(evt);}
                        public void dropActionChanged(DropTargetDragEvent evt) {fileAction(evt);}
                    }
            );
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }
    
    private void fileAction(DropTargetDragEvent evt) {
        if (isDragOk(evt)) evt.acceptDrag(DnDConstants.ACTION_COPY);
        else evt.rejectDrag();
    }
    
    private void fileDrop(DropTargetDropEvent evt) {
        try { // Get whatever was dropped
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            
            File[] files = getFiles(evt.getTransferable());
            if (files != null) {
                listener.filesDropped(files);
                evt.getDropTargetContext().dropComplete(true);
            } else {
                evt.rejectDrop();
            }
        } catch (IOException | UnsupportedFlavorException e) {
            e.printStackTrace();
            evt.rejectDrop();
        }
    }

    private File[] getFiles(final Transferable tr) throws IOException, UnsupportedFlavorException {
        if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            return files;
        } else {
            Optional<DataFlavor> flavor = Arrays.stream(tr.getTransferDataFlavors())
                    .filter(DataFlavor::isRepresentationClassReader).findFirst();
            if (flavor.isPresent()) {
                Reader reader = flavor.get().getReaderForText(tr);
                BufferedReader br = new BufferedReader(reader);
                return createFileArray(br);
            }
        }
        return null;
    }

    private static File[] createFileArray(BufferedReader bReader) {
        try {
            List<File> list = new ArrayList<>();
            for (String line; (line = bReader.readLine()) != null; ) {
                try {
                    if (("" + (char) 0).equals(line)) continue; // kde appends a 0 char to the end of the reader

                    File file = new File(new URI(line));
                    list.add(file);
                } catch (Exception ex) {}
            }
            return list.toArray(new File[list.size()]);
        } catch (IOException ex) {}
        return new File[0];
    }


    private boolean isDragOk(final DropTargetDragEvent evt) {
        boolean ok = evt.getCurrentDataFlavorsAsList().stream()
                .anyMatch(flavor -> flavor.equals(DataFlavor.javaFileListFlavor) || flavor.isRepresentationClassReader());
        try {
            final File[] files = getFiles(evt.getTransferable());
            ok = ok && validator.isDragOk(files);
        } catch (Exception e) {}
        return ok;
    }

}