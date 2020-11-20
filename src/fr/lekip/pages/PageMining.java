package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.GroundType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private GamePlayer player = new GamePlayer();
    private ProgressBar energyBar = new ProgressBar();

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes) throws FileNotFoundException {
        Image skyBox = new Image(
                new FileInputStream("src/assets/textures/pages/mining/skybox" + skyboxType.getId() + ".png"));

        // Create the map
        GameImage imgSkyBox = new GameImage(skyBox, 0, 0, skyBox.getWidth(), skyBox.getHeight(), true);
        add(imgSkyBox);

        int x = 0;
        int y = 262;
        try {
            for (int i = 0; i < GROUND_BLOCKS_NUMBER; i++) {
                if (i < 780) {
                    groundBox[i] = groundTypes.get(0).getGameImage();
                } else
                    groundBox[i] = groundTypes.get(1).getGameImage();

                // Drawing ground blocks
                x += 18;
                if (i % 81 == 0) {
                    y += 18;
                    x = 0;
                }

                groundBox[i].setX(x);
                groundBox[i].setY(y);
                add(groundBox[i]);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        add(player);
        loadEnergyBar();

        // Add map event handler
        addEventHandler(PlayerMovementsEventHandler.class);

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

        // Calculation of the maximal energy
        //
    }

    public void decreaseEnergy() {
        // to finish
        // energybar.setProgress(oldValue - energyConsumed in percentage ->) percentage
        // calculation : (newValue * 100) / initValue
    }
}