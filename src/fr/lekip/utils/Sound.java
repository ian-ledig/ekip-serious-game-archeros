package fr.lekip.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public enum Sound {
    MENU("src/assets/audio/musics/menu"),
    GAME("src/assets/audio/musics/game"),

    BUTTON("src/assets/audio/fx/button"),
    BUTTON_END("src/assets/audio/fx/buttonEnd"),
    QUIT("src/assets/audio/fx/quit"),
    LOSE("src/assets/audio/fx/lose"),
    WIN("src/assets/audio/fx/win"),

    SHOVEL("src/assets/audio/tools/shovel"),
    PICKAXE("src/assets/audio/tools/pickaxe"),
    PROBE("src/assets/audio/tools/probe"),
    DYNAMITER("src/assets/audio/tools/dynamiter");

    private final String soundPath;

    Sound(String soundPath){
        this.soundPath = soundPath;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public MediaPlayer getMediaPlayer(){
        return new MediaPlayer(new Media(new File(soundPath + ".mp3").toURI().toString()));
    }

    public MediaPlayer getMediaPlayer(int index){
        return new MediaPlayer(new Media(new File(soundPath + index + ".mp3").toURI().toString()));
    }
}
