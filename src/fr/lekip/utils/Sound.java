package fr.lekip.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Enum of sounds which is loaded by any page
 */
public enum Sound {
    MENU("src/assets/audio/musics/menu"),
    GAME("src/assets/audio/musics/game"),

    BUTTON("src/assets/audio/fx/button"),
    BUTTON_END("src/assets/audio/fx/buttonEnd"),
    QUIT("src/assets/audio/fx/quit"),
    LOSE("src/assets/audio/fx/lose"),
    WIN("src/assets/audio/fx/win"),
    PICK_UP("src/assets/audio/fx/pickUp"),
    BREAK("src/assets/audio/fx/break"),

    SHOVEL("src/assets/audio/tools/shovel"),
    PICKAXE("src/assets/audio/tools/pickaxe"),
    PROBE("src/assets/audio/tools/probe"),
    DYNAMITER("src/assets/audio/tools/dynamiter");

    private final String soundPath;

    Sound(String soundPath){
        this.soundPath = soundPath;
    }

    /**
     * @return sound path
     */
    public String getSoundPath() {
        return soundPath;
    }

    /**
     * @return media player of the sound
     */
    public MediaPlayer getMediaPlayer(){
        return new MediaPlayer(new Media(new File(soundPath + ".mp3").toURI().toString()));
    }

    /**
     * @param index index of the sound type
     * @return media player of the sound
     */
    public MediaPlayer getMediaPlayer(int index){
        return new MediaPlayer(new Media(new File(soundPath + index + ".mp3").toURI().toString()));
    }
}
