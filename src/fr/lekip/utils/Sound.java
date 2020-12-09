package fr.lekip.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public enum Sound {
    MENU("src/assets/audio/musics/menu"),
    GAME("src/assets/audio/musics/game"),

    BUTTON("src/assets/audio/musics/button"),
    QUIT("src/assets/audio/musics/quit"),

    SHOVEL("src/assets/audio/tools/shovel"),
    PICKAXE("src/assets/audio/tools/pickaxe"),
    PROBE("src/assets/audio/tools/probe"),
    DYNAMITER("src/assets/audio/tools/dynamiter");

    private final String soundPath;

    // We declare it here because otherwise MediaPlayer class will delete it before
    // the audio finish to play
    public static MediaPlayer audioPlayer;

    Sound(String soundPath){
        this.soundPath = soundPath;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public static void playSound(Sound sound, int state){
        Sound.audioPlayer = new MediaPlayer(new Media(new File(sound.getSoundPath() + ".mp3").toURI().toString()));
        if(state != 0)
            Sound.audioPlayer.setCycleCount(state);
        Sound.audioPlayer.play();
    }

    public static void playSound(Sound sound, int state, int index){
        Sound.audioPlayer = new MediaPlayer(new Media(new File(sound.getSoundPath() + index + ".mp3").toURI().toString()));
        if(state != 0)
            Sound.audioPlayer.setCycleCount(state);
        Sound.audioPlayer.play();
    }

    public static void stopSound(){
        Sound.audioPlayer.stop();
    }
}
