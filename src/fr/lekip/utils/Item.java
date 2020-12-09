package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Item {

    COIN("Pièce", "La piece est cool", 1, 1, "src/assets/textures/items/coin.png", 36),
    BUTTON("Bouton", "le bouton est nice", 1, 1, "src/assets/textures/items/button.png", 36),
    PRIEST("Prêtre du moyen âge", "Pretre du moyen age surement mort a cause de jsp", 1, 2,
            "src/assets/textures/items/priest.png", 36),
    NAIL("Instrument de mesure", "Instrument de mesure ressemblant étrangement à un clou", 1, 1,
            "src/assets/textures/items/nail.png", 36),

    MICROLITH_SCRAPER("Grattoir en microlithe", "La piece est cool", 1, 1, "src/assets/textures/items/microlithScraper.png", 36),
    YORKSHIRE_SCRAPER("Grattoir en silex de yorkshire", "La piece est cool", 1, 1, "src/assets/textures/items/yorkshireScraper.png", 36),
    COMB("Peigne gallo-romain", "La piece est cool", 1, 1, "src/assets/textures/items/comb.png", 36),
    STATUETTE("Statuette", "La piece est cool", 1, 1, "src/assets/textures/items/statuette.png", 36),

    STATUETTE_BRONZE("Statuette en bronze d'Artémis", "La piece est cool", 1, 1, "src/assets/textures/items/statuetteBronze.png", 36),
    STATUE("Fragments de statue d’un général romain", "La piece est cool", 1, 1, "src/assets/textures/items/statue.png", 36),
    ARTEFACT("Artefact", "La piece est cool", 1, 1, "src/assets/textures/items/artefact.png", 36),
    STATUETTE_GOLD("Statuette", "La piece est cool", 1, 1, "src/assets/textures/items/statuetteGold.png", 36);

    public static final int MAX_ITEM_SIZE = 36;

    private String name;
    private String lore;
    private int minResistance;
    private int maxResistance;
    private GameImage gameImage;
    private String texturePath;

    Item(String name, String lore, int minResistance, int maxResistance, String texturePath, int size) {
        this.name = name;
        this.lore = lore;
        this.minResistance = minResistance;
        this.maxResistance = maxResistance;
        this.texturePath = texturePath;

        try {
            this.gameImage = new GameImage(new Image(new FileInputStream(texturePath)), 0, 0, size, size, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getTexturePath() {
        return texturePath;
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
