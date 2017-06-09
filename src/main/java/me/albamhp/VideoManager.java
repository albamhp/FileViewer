package me.albamhp;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.albamhp.gui.components.PlayButton;
import me.albamhp.gui.components.StopButton;
import me.albamhp.gui.components.TimeLine;
import me.albamhp.gui.components.VideoPlayer;
import me.albamhp.gui.components.WheelDisplayer;

public class VideoManager {
    private VideoPlayer player;
    private TimeLine timeLine;
    private PlayButton playB;
    private StopButton stopB;
    private WheelDisplayer wheelDisplayer;
    
    public VideoManager(VideoPlayer player, TimeLine timeLine, PlayButton playB, StopButton stopB, WheelDisplayer disp) {
        this.player = player;
        this.timeLine = timeLine;
        this.playB = playB;
        this.stopB = stopB;
        this.wheelDisplayer = disp;
        
        player.init(this);
        timeLine.init(this);
        playB.init(this);
        stopB.init(this);
        wheelDisplayer.init(this);
    }
    
    // Called after a video is dragged
    public void reload() {
        playB.setIcon(true);
        timeLine.setValue(0);
        timeLine.setMaximum((int) getPlayer().getTotalDuration().toMillis());
        getPlayer().currentTimeProperty().addListener((observable, oldValue, newValue) -> this.update(newValue));
    }
    
    public MediaPlayer getPlayer() {
        return player.getPlayer();
    }
    
    public void togglePlay() {
        if (player.isReady()) {
            setPlaying(!player.isState(MediaPlayer.Status.PLAYING, MediaPlayer.Status.STALLED));
        }
    }
    
    public void stop() {
        if (player.isReady()) {
            setStopped();
        }
    }
    
    private void setPlaying(boolean playing) {
        playB.setIcon(!playing);
        if (playing) getPlayer().play();
        else getPlayer().pause();
    }
    
    private void setStopped() {
        getPlayer().stop();
        playB.setIcon(true);
    }
    
    public void update() {
        MediaPlayer player = getPlayer();
        if (player != null)
            update(player.getCurrentTime());
    }
    
    public void update(Duration duration) {
        int millis = (int) duration.toMillis();
        timeLine.setValue(millis);
        wheelDisplayer.updateTo(millis);
    }
    
    public void setVideoAt(int millis) {
        wheelDisplayer.updateTo(millis);
        if (getPlayer() != null) getPlayer().seek(Duration.millis(millis));
    }
    
}
