package me.albamhp.gui.components;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import me.albamhp.VideoManager;
import me.albamhp.utils.FileDrop;
import me.albamhp.utils.FileDropBuilder;

import java.io.File;
import java.util.Arrays;

public class VideoPlayer extends JFXPanel implements VideoComponent {
    
    private VideoManager manager;
    
    public void init(VideoManager manager) {
        this.manager = manager;
    }
    
    private StackPane root = new StackPane();
    private MediaView mediaView = null;
    
    public VideoPlayer() {
        Scene scene = new Scene(root, Color.BLACK);
        setScene(scene);
        setVisible(true);

        new FileDropBuilder(this, (File[] files) -> {
            try {
                Media media = new Media(files[0].toURI().toURL().toString());
            
                Platform.runLater(() -> setVideo(media));
            } catch (Exception e) {
                // TODO: better exception handling, tell the user something went wrong
                throw new RuntimeException(e);
            }
        }).validate((File[] files) -> files.length == 1).build();
    }
    
    private void setVideo(Media media) {
        MediaPlayer player = new MediaPlayer(media);
        player.setOnReady(() -> manager.reload());
    
        if (mediaView != null) {
            mediaView.getMediaPlayer().dispose();
            mediaView.setMediaPlayer(player);
        } else {
            createView(player);
        }
    }
    
    private void createView(MediaPlayer player) {
        mediaView = new MediaView(player);

        mediaView.fitWidthProperty().bind(getScene().widthProperty());
        mediaView.fitHeightProperty().bind(getScene().heightProperty());
        mediaView.setPreserveRatio(true);

        root.getChildren().add(new BorderPane(mediaView));
    }
    
    public MediaPlayer getPlayer() {
        return mediaView == null ? null : mediaView.getMediaPlayer();
    }
    
    public boolean isReady() {
        return mediaView != null && isState(MediaPlayer.Status.READY, MediaPlayer.Status.PLAYING, MediaPlayer.Status.PAUSED, MediaPlayer.Status.STOPPED, MediaPlayer.Status.STALLED);
    }
    
    public boolean isState(MediaPlayer.Status... status) {
        return Arrays.asList(status).contains(mediaView.getMediaPlayer().getStatus());
    }
    
}
