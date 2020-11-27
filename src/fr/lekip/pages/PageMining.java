package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.GroundType;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    public static final int GROUND_BLOCKS_LINE_NUMBER = 81;
    public static final String SCORE_BASE_TEXT = "Objets : ";
    private List<GameImage> groundItems = new ArrayList<>();
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private GamePlayer player = new GamePlayer(this);
    private ProgressBar energyBar = new ProgressBar();
    private Label score;
    private int itemFoundCount = 0;

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
            // Item spawning
            for (Item item : items) {
                int spawnPosX = (int) (36 + Math.random() * 1422);
                int spawnPosY = (int) (298 + Math.random() * 450);
                GameImage newItem = item.cloneGameImage();
                newItem.setX(spawnPosX);
                newItem.setY(spawnPosY);
                add(newItem);
                groundItems.add(newItem);

                // Delete the item and increase the number of objects found when item is clicked
                newItem.setOnMouseClicked(mouseEvent -> {
                    newItem.setImage(null);
                    itemFoundCount++;
                    score.setText(SCORE_BASE_TEXT + itemFoundCount + "/" + groundItems.size());

                    if(isEnd()){
                        // TO DO : End the party
                    }
                });
            }

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
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        add(player);

        loadEnergyBar();
        loadLabels();

        // Add map event handler
        addEventHandler(PlayerMovementsEventHandler.class);
    }

    public boolean isEnd(){
        return
                itemFoundCount == groundItems.size() - 1 ||
                energyBar.getProgress() <= 0;
    }

    public List<GameImage> getGroundItems() {
        return groundItems;
    }

    public void setGroundItems(List<GameImage> groundItems) {
        this.groundItems = groundItems;
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

    public void setEnergyBar(ProgressBar energyBar) {
        this.energyBar = energyBar;
    }

    public int getItemFoundCount() {
        return itemFoundCount;
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

    public void loadLabels() throws FileNotFoundException {
        score = new Label(SCORE_BASE_TEXT + itemFoundCount + "/" + groundItems.size());
        score.setFont(Font.loadFont(new FileInputStream(new File("src/assets/font/bebas_neue/BebasNeue-Regular.ttf")), 27.0));
        HBox hbox = new HBox(20);
        hbox.setTranslateX(1300);
        hbox.setTranslateY(80);
        hbox.setSpacing(5);
        hbox.getChildren().add(score);
        add(hbox);
    }

    public void decreaseEnergy() {
        // to finish
        // energybar.setProgress(oldValue - energyConsumed in percentage ->) percentage
        // calculation : (newValue * 100) / initValue
    }
}