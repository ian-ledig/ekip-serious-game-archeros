package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Item {

    ARTEFACT01("", "", 3, 5,"src/assets/textures/items/artefact01.png", 36);

    public static final int MAX_ITEM_SIZE = 36;

    private String name;
    private String lore;
    private int minResistance;
    private int maxResistance;
    private GameImage gameImage;

    Item(String name, String lore, int minResistance, int maxResistance, String texturePath, int size){
        this.name = name;
        this.lore = lore;
        this.minResistance = minResistance;
        this.maxResistance = maxResistance;

        try {
            this.gameImage = new GameImage(new Image(new FileInputStream(texturePath)), 0, 0, size, size, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getMinResistance() {
        return minResistance;
    }

    public int getMaxResistance() {
        return maxResistance;
    }

    public double getTextureSize() {
        return gameImage.getFitWidth();
    }

    public GameImage cloneGameImage() throws CloneNotSupportedException {
        return (GameImage) gameImage.clone();
    }
}
