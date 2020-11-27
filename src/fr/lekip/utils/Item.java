package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Item {

    ARTEFACT01("", "", "src/assets/textures/items/artefact01.png", 36);

    private String name;
    private String lore;
    private GameImage gameImage;

    Item(String name, String lore, String texturePath, int size){
        this.name = name;
        this.lore = lore;

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

    public GameImage cloneGameImage() throws CloneNotSupportedException {
        return (GameImage) gameImage.clone();
    }
}
