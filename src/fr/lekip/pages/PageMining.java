package fr.lekip.pages;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.GroundType;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import fr.lekip.utils.Tool;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    public static final int GROUND_BLOCKS_LINE_NUMBER = 81;
    public static final int GROUND_BLOCKS_ROW_NUMBER = 27;
    public static final String SCORE_BASE_TEXT = "Objets : ";

    private final SkyboxType skyboxType;
    private final List<GroundType> groundTypes;
    private final List<Item> items;
    private final int nextLayerIndex;
    private Item itemWin;

    private final Button btnPause = new Button("II");
    private final Button btnResume = new Button("Reprendre");
    private final Button btnRestart = new Button("Recommencer");
    private final Button btnAbandon = new Button("Abandonner");

    private final List<Item> itemsFound = new ArrayList<>();
    private final List<Item> itemsLost = new ArrayList<>();

    private int itemsRemaining;

    private List<GameImage> groundItems = new ArrayList<>();
    private GameImage groundItemWin;
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private GamePlayer player = new GamePlayer(this);
    private ProgressBar energyBar = new ProgressBar();

    private double energyValue = 0;
    private double energyDefault = 0;

    private Label score;

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes, List<Item> items, int nextLayerIndex)
            throws FileNotFoundException {
        this.skyboxType = skyboxType;
        this.groundTypes = groundTypes;
        this.items = items;
        this.nextLayerIndex = nextLayerIndex;

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
                if(i < GROUND_BLOCKS_LINE_NUMBER){
                    groundBox[i] = groundTypes.get(0).cloneGameImage();
                } else if (i < nextLayerIndex) {
                    groundBox[i] = groundTypes.get(1).cloneGameImage();
                } else
                    groundBox[i] = groundTypes.get(2).cloneGameImage();

                // Drawing ground blocks
                x += GroundType.GROUND_SIZE;
                if (i % GROUND_BLOCKS_LINE_NUMBER == 0) {
                    y += GroundType.GROUND_SIZE;
                    x = 0;
                }

                groundBox[i].setX(x);
                groundBox[i].setY(y);
                add(groundBox[i]);
            }

            this.itemsRemaining = this.items.size();
            boolean itemWinAssigned = false;
            // Item spawning
            for (Item item : items) {
                double itemSize = item.getTextureSize();
                boolean correctPos = false;
                int spawnPosX = 0;
                int spawnPosY = 0;

                // Try to create a position far enough away from other items
                while(!correctPos){
                    correctPos = true;
                    spawnPosX = (int) ((Math.random() * (GROUND_BLOCKS_LINE_NUMBER * GroundType.GROUND_SIZE - Item.MAX_ITEM_SIZE * 2 - Item.MAX_ITEM_SIZE * 2)) + Item.MAX_ITEM_SIZE);
                    spawnPosY = (int) ((Math.random() * (GROUND_BLOCKS_ROW_NUMBER * GroundType.GROUND_SIZE - Item.MAX_ITEM_SIZE * 2)) + 262);

                    if(!groundItems.isEmpty()){
                        for(GameImage imgItem : groundItems){
                            if(
                                    spawnPosX + itemSize >= imgItem.getXImage()  &&
                                    spawnPosX <= imgItem.getXImage() + imgItem.getFitWidth() ||
                                    spawnPosY + itemSize >= imgItem.getYImage()  &&
                                    spawnPosY <= imgItem.getYImage() + imgItem.getFitHeight() ||
                                    Math.abs(Math.sqrt(Math.pow(spawnPosX - imgItem.getXImage(), 2) + Math.pow(spawnPosY - imgItem.getYImage(), 2))) < 200
                            ){
                                correctPos = false;
                                break;
                            }
                        }
                    }
                }

                // Add and position the item
                GameImage newItem = item.cloneGameImage();
                newItem.setX(spawnPosX);
                newItem.setY(spawnPosY);
                add(newItem);

                // Try to set item to mandatory object
                if(!itemWinAssigned && (item == items.get(items.size() - 1) || (int) (Math.random() * 2) == 0)){
                    itemWinAssigned = true;
                    groundItemWin = newItem;
                    itemWin = item;
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-1);
                    newItem.setEffect(colorAdjust);
                }
                groundItems.add(newItem);

                // Delete the item and increase the number of objects found when item is clicked
                newItem.setOnMouseClicked(mouseEvent -> {
                    if(newItem.getImage() != null){

                        Tool tool = player.getTool();

                        // If the used tool is strong enough
                        // Try to add the item to the founded or lost lists
                        if(tool != null && tool.getStrength() >= item.getMinResistance()){
                            if(tool.getStrength() <= item.getMaxResistance()){
                                this.itemsFound.add(item);
                            }
                            else{
                                this.itemsLost.add(item);
                                itemsRemaining--;
                                score.setTextFill(Color.INDIANRED);
                            }

                            // If the mandatory item is found : set it to null
                            if(newItem.equals(groundItemWin))
                                groundItemWin = null;

                            newItem.setImage(null);
                            score.setText(SCORE_BASE_TEXT + this.itemsFound.size() + "/" + itemsRemaining);

                            // Check if the game can be stop
                            tryToEndGame();
                        }
                    }
                });
            }

            // Calculation of the energy max
            double Y1 = 0;
            double xG = 1450;
            double xD = 0;
            for (int i = 0; i < groundItems.size(); i++) {
                energyDefault += (getGroundItems().get(i).getYImage() - 262) * 1.2 + 7;

                // We take the deepest item Y
                if ((getGroundItems().get(i).getYImage() - 262) > Y1) {
                    Y1 = (getGroundItems().get(i).getYImage() - 262);
                }

                // We save the first item in X basis and the last item
                if ((getGroundItems().get(i).getXImage()) < xG) {
                    xG = getGroundItems().get(i).getXImage();
                }
                if ((getGroundItems().get(i).getXImage()) > xD) {
                    xD = getGroundItems().get(i).getXImage();
                }

            }
            energyDefault += (xD - xG);

            // We calculate the malus
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

            // If the malus is less than 180, we give him a malus of energy
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
        loadLabels();
        loadPause();

        // Add player movements event handler
        addEventHandler(PlayerMovementsEventHandler.class);
    }

    public void tryToEndGame(){
        if(isEnd()){
            System.out.println("END !");
        }
    }
    
    public boolean isEnd(){
        return
                groundItemWin == null && !itemsFound.contains(itemWin)||
                itemsFound.size() + itemsLost.size() == groundItems.size() ||
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

    public List<Item> getItemsFound() {
        return itemsFound;
    }

    public List<Item> getItemsLost() {
        return itemsLost;
    }

    public List<GroundType> getGroundTypes() {
        return groundTypes;
    }

    public int getNextLayerIndex() {
        return nextLayerIndex;
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
            Tool tool = player.getTool();
            if(tool != null)
                decreaseEnergy(player.getTool().getStrength());
        });

        // Calculation of the maximal energy
        //
    }

    public void decreaseEnergy(int strength) {
        energyValue -= strength * 18;
        energyBar.setProgress(((energyValue * 100) / energyDefault) * 0.01);
    }

    public void loadLabels() throws FileNotFoundException {
        score = new Label(SCORE_BASE_TEXT + "0/" + groundItems.size());
        score.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/bebas_neue/BebasNeue-Regular.ttf")), 27.0));
        HBox hbox = new HBox(20);
        hbox.setTranslateX(1300);
        hbox.setTranslateY(80);
        hbox.setSpacing(5);
        hbox.getChildren().add(score);
        add(hbox);
    }

    public void loadPause() {
        btnPause.setMinSize(40, 40);
        HBox hbox = new HBox(20);
        hbox.setTranslateX(20);
        hbox.setTranslateY(40);
        hbox.getChildren().add(btnPause);
        add(hbox);

        btnPause.setOnAction( event -> {

            VBox vbox = new VBox();
            vbox.setPrefWidth(150);
            btnResume.setMinHeight(30);
            btnRestart.setMinHeight(30);
            btnAbandon.setMinHeight(30);
            btnResume.setMinWidth(vbox.getPrefWidth());
            btnRestart.setMinWidth(vbox.getPrefWidth());
            btnAbandon.setMinWidth(vbox.getPrefWidth());
            vbox.setTranslateX(650);
            vbox.setTranslateY(300);
            vbox.setSpacing(25);
            vbox.getChildren().addAll(btnResume, btnRestart, btnAbandon);

            add(vbox);

            btnResume.setVisible(true);
            btnRestart.setVisible(true);
            btnAbandon.setVisible(true);
        });

        btnResume.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               //Retirer pause
               btnResume.setVisible(false);
               btnRestart.setVisible(false);
               btnAbandon.setVisible(false);
            }
        });

        btnRestart.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Main.setShowedPage(new PageMining(skyboxType, groundTypes, items, nextLayerIndex));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAbandon.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tryToEndGame();
            }
        });
    }
}