package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum GroundType {

    DIRT("src/assets/textures/pages/mining/dirt.png", 1),
    STONE("src/assets/textures/pages/mining/stone.png", 10)
    ;

    public static final int GROUND_SIZE = 18;

    private GameImage gameImage;
    private int resistance;

    GroundType(String texturePath, int resistance) {
        try {
            this.gameImage = new GameImage(new Image(new FileInputStream(texturePath)), 0, 0, 18, 18, true);
            this.resistance = resistance;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public GameImage cloneGameImage() throws CloneNotSupportedException {
        return (GameImage) gameImage.clone();
    }

    public int getResistance() {
        return resistance;
    }
}
