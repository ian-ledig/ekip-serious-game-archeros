package fr.lekip.utils;

import fr.lekip.components.GameImage;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Item {

    COIN("Pièce", "Pièce du 18ème siècle utilisé par la Géorgie, Diametre : 25mm", 1, 1,
            "src/assets/textures/items/coin.png", 36),
    BUTTON("Bouton", "", 1, 1, "src/assets/textures/items/button.png", 36),
    PRIEST("Prêtre du moyen âge", "Visage d'un prêtre du 12e ou du 13e siècle, âge compris entre 35 et 45 ans", 1, 2,
            "src/assets/textures/items/priest.png", 36),
    NAIL("Instrument de mesure",
            "Epingle à tête ronde : tête pleine, l'arbre de la broche est de section circulaire avec toutes les rainures. Régularité dans la composition et la taille, 31 mm sur 1,2 mm indique une production de masse. Ces articles étaient omniprésents et régulièrement utilisé pour épingler des vêtements et des tenues.",
            1, 1, "src/assets/textures/items/nail.png", 36),
    MICROLITH_SCRAPER("Grattoir en microlithe",
            "Grattoir en microlithe, dimension : 23mm x 24mm. Désigne un outil de pierre taillée obtenu en retouchant une lame ou un éclat. ",
            1, 1, "src/assets/textures/items/microlithScraper.png", 36),
    YORKSHIRE_SCRAPER("Grattoir en silex de yorkshire",
            "Grattoir en silex de Yorkshire, dimension : 23mm x 24mm. Désigne un outil de pierre taillée obtenu en retouchant une lame ou un éclat.",
            1, 1, "src/assets/textures/items/yorkshireScraper.png", 36),
    COMB("Peigne gallo-romain",
            "Peigne à deux rangées de dents. Dimension : 5cm. Date : -50 avant J.C. Le peigne traditionnel des Romains était monobloc, rectangulaire, avec les petits côtés généralement en arc de cercle ou en accolade. Les dents, plus fines sur un bord que sur l'autre, étaient ménagées sur les longs côtés. Les peignes pouvaient être en bois ou en métal, seuls les petits peignes étaient en os car le matériau n'offre pas suffisamment de surface.",
            1, 1, "src/assets/textures/items/comb.png", 36),
    STATUETTE("Statuette", "Matériaux et techniques : Terre cuite Dimensions et poids : 28,6 x 17,3 x 16,1 cm, 1804 g",
            1, 1, "src/assets/textures/items/statuette.png", 36),
    STATUETTE_BRONZE("Statuette en bronze d'Artémis",
            "Petite statuette en bronze doré représentant une Artémis du type d’Ephèse, Hauteur : 60cm. Date : IIème Siècle. Représentant la déesse Artémis",
            1, 1, "src/assets/textures/items/statuetteBronze.png", 36),
    STATUE("Fragments de statue d’un général romain",
            "Trois fragments d’une statue en marbre appartenant à un homme de taille imposante, mesurant plus de 2,50m de hauteur et portant un vêtement jusqu’à mi-hauteur du biceps.",
            1, 1, "src/assets/textures/items/statue.png", 36),
    ARTEFACT("Artefact",
            "Pièce et artefact datant de l'âge du bronze grec. 2800 avant J.-C. et a duré jusqu'à 1050 avant J.-C. Dimension : 25mm",
            1, 1, "src/assets/textures/items/artefact.png", 36),
    STATUETTE_G("Statuette",
            "Statuette Cyclade. La culture des Cyclades est une culture archéologique du Néolithique final et du début de l'Âge du bronze, qui s'est développée dans les iles de l'archipel des Cyclades au IIIe millénaire av. J.-C. Hauteur : 25 cm.",
            1, 1, "src/assets/textures/items/statuetteGold.png", 36);

    public static final int MAX_ITEM_SIZE = 36;

    private final String name;
    private final String lore;
    private final int minResistance;
    private final int maxResistance;
    private final String texturePath;

    private GameImage gameImage;

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
