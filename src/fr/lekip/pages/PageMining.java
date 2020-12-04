package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.GroundType;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    public static final int GROUND_BLOCKS_LINE_NUMBER = 81;
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private GamePlayer player = new GamePlayer(this);
    private ProgressBar energyBar = new ProgressBar();
    private double energyValue = 0;
    private double energyDefault = 0;

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes, List<Item> items)
            throws FileNotFoundException {
        Image skyBox = new Image(
                new FileInputStream("src/assets/textures/pages/mining/skybox" + skyboxType.getId() + ".png"));

        // Create the map
        GameImage imgSkyBox = new GameImage(skyBox, 0, 0, skyBox.getWidth(), skyBox.getHeight(), true);
        add(imgSkyBox);

        int x = 0;
        int y = 262;
        try {

            // Ground box spawning
            for (int i = 0; i < GROUND_BLOCKS_NUMBER; i++) {
                if (i < 780) {
                    groundBox[i] = groundTypes.get(0).cloneGameImage();
                } else
                    groundBox[i] = groundTypes.get(1).cloneGameImage();

                // Drawing ground blocks
                x += 18;
                if (i % GROUND_BLOCKS_LINE_NUMBER == 0) {
                    y += 18;
                    x = 0;
                }

                groundBox[i].setX(x);
                groundBox[i].setY(y);
                add(groundBox[i]);
            }

            // Item spawning
            for (Item item : items) {
                int spawnPosX = (int) (36 + Math.random() * 1422);
                int spawnPosY = (int) (298 + Math.random() * 450);
                GameImage newItem = item.cloneGameImage();
                newItem.setX(spawnPosX);
                newItem.setY(spawnPosY);
                add(newItem);
            }

            // Calculation of the energy max
            int Y1 = 0;
            int xG = 1450;
            int xD = 0;
            for (int i = 0; i < 4; i++) {
                energyDefault += (getGroundItems().get(i).getYImage() - 262) * 1.2 + 7;

                // TODO Prendre le plus grand Y et le petit Y de la liste
                if ((getGroundItems().get(i).getYImage() - 262) > Y1) {
                    Y1 = (getGroundItems().get(i).getYImage() - 262);
                }

                if ((getGroundItems().get(i).getXImage()) < xG) {
                    xG = getGroundItems().get(i).getXImage();
                }
                if ((getGroundItems().get(i).getXImage()) > xD) {
                    xD = getGroundItems().get(i).getXImage();
                }

            }
            energyDefault += (xD - xG);

            double malus;
            malus = Y1 * 3;
            for (GameImage item : getGroundItems()) {
                if (item.getY() != Y1) {
                    malus -= item.getY();
                }
            }

            /*
             * valeur absolu de [Y de l'item le plus profond x (nombre d'items - 1) - (Y de
             * tous les autres objets)] -> a si a < 10 energieMax - ([10 - a] x 2)
             */

            if (malus < 0) {
                malus *= -1;
            }
            if (malus < 180) {
                energyDefault -= (180 - malus) * 2;
            }

            energyValue = energyDefault;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        add(player);
        loadEnergyBar();

        // Add map event handler
        addEventHandler(PlayerMovementsEventHandler.class);
    }

    public GameImage[] getGroundBox() {
        return groundBox;
    }

    public void setGroundBox(GameImage[] groundBox) {
        this.groundBox = groundBox;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public void loadEnergyBar() throws FileNotFoundException {

        // Init energyBar
        energyBar.setProgress(1);
        energyBar.setPrefSize(300, 30);
        energyBar.setStyle("-fx-accent: orange");

        GameImage lightning = new GameImage(
                new Image(new FileInputStream("src/assets/textures/pages/mining/lightning.png")), 0, 0, 50, 50, true);
        HBox hbox = new HBox(20);
        hbox.setTranslateX(1050);
        hbox.setTranslateY(40);
        hbox.setSpacing(5);
        hbox.getChildren().addAll(lightning, energyBar);
        add(hbox);
        setOnMouseClicked((e) -> {
            decreaseEnergy(player.getTool().getStrength());
        });

        // Calculation of the maximal energy
        //
    }

    public void decreaseEnergy(int strength) {
        System.out.println(energyValue);
        energyValue -= strength * 18;
        System.out.println(((energyValue * 100) / energyDefault) * 0.01);
        energyBar.setProgress(((energyValue * 100) / energyDefault) * 0.01);
        // to finish
        // energybar.setProgress(oldValue - energyConsumed in percentage ->) percentage
        // calculation : (newValue * 100) / initValue
    }
}