package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Enum of ground types which is used in page mining
 */
public enum GroundType {

    GRASS("src/assets/textures/pages/mining/grass.png", 1),
    DIRT("src/assets/textures/pages/mining/dirt.png", 1),
    STONE("src/assets/textures/pages/mining/stone.png", 2),
    SAND("src/assets/textures/pages/mining/sand.png", 1),
    SANDSTONE("src/assets/textures/pages/mining/sandstone.png", 1)
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

    /**
     * @return game image cloned from the ground type
     * @throws CloneNotSupportedException
     */
    public GameImage cloneGameImage() throws CloneNotSupportedException {
        return (GameImage) gameImage.clone();
    }

    /**
     * @return resistance of the ground type
     */
    public int getResistance() {
        return resistance;
    }
}
