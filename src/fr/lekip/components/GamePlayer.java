package fr.lekip.components;

import fr.lekip.utils.Tools;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GamePlayer extends GameImage{

    public static final String PLAYER_TEXTURE_IDLE = "src/assets/textures/player/player_idle.png";
    public static final String PLAYER_TEXTURE_RIGHT = "src/assets/textures/player/player_right.png";
    public static final String PLAYER_TEXTURE_LEFT = "src/assets/textures/player/player_left.png";

    private Tools tool;

    public GamePlayer() throws FileNotFoundException {
        super(new Image(new FileInputStream(PLAYER_TEXTURE_IDLE)), 20, 185, 55, 98, true);
    }


    public void decrementX(int delta){
        setX(getX() - delta);

        try {
            setImage(new Image(new FileInputStream(PLAYER_TEXTURE_LEFT)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void incrementX(int delta){
        setX(getX() + delta);

        try {
            setImage(new Image(new FileInputStream(PLAYER_TEXTURE_RIGHT)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void decrementY(int delta){
        setY(getY() - delta);
    }

    public void incrementY(int delta){
        setY(getY() + delta);
    }
}
