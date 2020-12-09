package fr.lekip.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public enum Sound {
    PROBE("src/assets/audio/tools/probe.mp3");

    private String soundPath;

    Sound(String soundPath){
        this.soundPath = soundPath;
    }

    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer(new Media(new File(soundPath).toURI().toString()));
    }
}
