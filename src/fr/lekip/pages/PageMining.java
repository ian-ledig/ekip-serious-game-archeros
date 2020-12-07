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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import fr.lekip.utils.Tool;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    public static final int GROUND_BLOCKS_LINE_NUMBER = 81;
    public static final int GROUND_BLOCKS_ROW_NUMBER = 27;
    public final Font FONT;
    public static final String SCORE_BASE_TEXT = "Objets : ";

    private final SkyboxType skyboxType;
    private final List<GroundType> groundTypes;
    private final List<Item> items;
    private final int nextLayerIndex;
    private Item itemWin;

    private final GameImage btnPause = new GameImage(
            new Image(new FileInputStream("src/assets/textures/pages/mining/btnPause.png")), 0, 0, 40, 40, true);
    private final GameImage btnResume;
    private final GameImage btnRestart;
    private final GameImage btnAbandon;

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

    private Label scoreMandatory;
    private Label score;

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes, List<Item> items, int nextLayerIndex)
            throws FileNotFoundException, CloneNotSupportedException {
        FONT = Font.loadFont(new FileInputStream(new File("src/assets/font/bebas_neue/BebasNeue-Regular.ttf")), 27.0);
        this.skyboxType = skyboxType;
        this.groundTypes = groundTypes;
        this.items = items;
        this.nextLayerIndex = nextLayerIndex;
        this.btnResume = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/mining/btn.png")), 0, 0,
                150, 30, true);
        this.btnRestart = (GameImage) btnResume.clone();
        this.btnAbandon = (GameImage) btnResume.clone();

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
                if (i < GROUND_BLOCKS_LINE_NUMBER) {
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
                while (!correctPos) {
                    correctPos = true;
                    spawnPosX = (int) ((Math.random() * (GROUND_BLOCKS_LINE_NUMBER * GroundType.GROUND_SIZE
                            - Item.MAX_ITEM_SIZE * 2 - Item.MAX_ITEM_SIZE * 2)) + Item.MAX_ITEM_SIZE);
                    spawnPosY = (int) ((Math.random()
                            * (GROUND_BLOCKS_ROW_NUMBER * GroundType.GROUND_SIZE - Item.MAX_ITEM_SIZE * 2)) + 262);

                    if (!groundItems.isEmpty()) {
                        for (GameImage imgItem : groundItems) {
                            if (spawnPosX + itemSize >= imgItem.getXImage()
                                    && spawnPosX <= imgItem.getXImage() + imgItem.getFitWidth()
                                    || spawnPosY + itemSize >= imgItem.getYImage()
                                            && spawnPosY <= imgItem.getYImage() + imgItem.getFitHeight()
                                    || Math.abs(Math.sqrt(Math.pow(spawnPosX - imgItem.getXImage(), 2)
                                            + Math.pow(spawnPosY - imgItem.getYImage(), 2))) < 200) {
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
                if (!itemWinAssigned && (item == items.get(items.size() - 1) || (int) (Math.random() * 3) == 0)) {
                    itemWinAssigned = true;
                    groundItemWin = newItem;
                    itemWin = item;
                }
                groundItems.add(newItem);

                // Delete the item and increase the number of objects found when item is clicked
                newItem.setOnMouseClicked(mouseEvent -> {
                    if (newItem.getImage() != null) {

                        Tool tool = player.getTool();

                        // If the used tool is strong enough
                        // Try to add the item to the founded or lost lists
                        if (tool != null && tool.getStrength() >= item.getMinResistance()) {
                            if (tool.getStrength() <= item.getMaxResistance()) {
                                this.itemsFound.add(item);
                            } else {
                                this.itemsLost.add(item);
                                itemsRemaining--;
                                score.setTextFill(Color.INDIANRED);
                            }

                            // If the mandatory item is found : set it to null
                            if (newItem.equals(groundItemWin)) {
                                groundItemWin = null;
                                if (itemsFound.contains(item))
                                    scoreMandatory.setTextFill(Color.DARKGREEN);
                            }

                            newItem.setImage(null);
                            score.setText(SCORE_BASE_TEXT + this.itemsFound.size() + "/" + itemsRemaining);

                            // Check if the game can be stop
                            tryToEndGame(false);
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

    public void tryToEndGame(boolean force) {
        if (force || isEnd()) {
            System.out.println("END !");
        }
    }

    public boolean isEnd() {
        return groundItemWin == null && !itemsFound.contains(itemWin)
                || itemsFound.size() + itemsLost.size() == groundItems.size() || energyBar.getProgress() <= 0;
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
            if (tool != null)
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
        scoreMandatory = new Label("Objectif : ");
        scoreMandatory.setFont(FONT);
        HBox hbxScoreMand = new HBox(20);
        hbxScoreMand.setTranslateX(1285);
        hbxScoreMand.setTranslateY(71);
        hbxScoreMand.setSpacing(5);
        hbxScoreMand.getChildren().add(scoreMandatory);
        add(hbxScoreMand);

        try {
            GameImage imgItemWin = itemWin.cloneGameImage();
            imgItemWin.setLayoutX(1370);
            imgItemWin.setLayoutY(71);
            add(imgItemWin);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        score = new Label(SCORE_BASE_TEXT + "0/" + groundItems.size());
        score.setFont(FONT);
        HBox hbxScore = new HBox(20);
        hbxScore.setTranslateX(1300);
        hbxScore.setTranslateY(100);
        hbxScore.setSpacing(5);
        hbxScore.getChildren().add(score);
        add(hbxScore);
    }

    public void loadPause() {
        StackPane spnPause = new StackPane();
        Text txtPause = new Text("II");
        txtPause.setFont(FONT);
        spnPause.setTranslateX(20);
        spnPause.setTranslateY(40);
        spnPause.setAlignment(Pos.CENTER);
        spnPause.getChildren().addAll(btnPause, txtPause);
        add(spnPause);

        StackPane spnResume = new StackPane();
        Text txtResume = new Text("Reprendre");
        txtResume.setFont(FONT);
        spnResume.setAlignment(Pos.CENTER);
        spnResume.getChildren().addAll(btnResume, txtResume);
        add(spnResume);

        StackPane spnRestart = new StackPane();
        Text txtRestart = new Text("Recommencer");
        txtRestart.setFont(FONT);
        spnRestart.setAlignment(Pos.CENTER);
        spnRestart.getChildren().addAll(btnRestart, txtRestart);
        add(spnRestart);

        StackPane spnAbandon = new StackPane();
        Text txtAbandon = new Text("Abandonner");
        txtAbandon.setFont(FONT);
        spnAbandon.setAlignment(Pos.CENTER);
        spnAbandon.getChildren().addAll(btnAbandon, txtAbandon);
        add(spnAbandon);

        VBox vbxPause = new VBox();
        vbxPause.setPrefWidth(150);
        vbxPause.setTranslateX(650);
        vbxPause.setTranslateY(300);
        vbxPause.setSpacing(25);
        vbxPause.getChildren().addAll(spnResume, spnRestart, spnAbandon);
        vbxPause.setVisible(false);
        add(vbxPause);

        spnPause.setOnMouseClicked(event -> {
            vbxPause.setVisible(!vbxPause.isVisible());
            spnResume.setVisible(true);
            spnRestart.setVisible(true);
            spnAbandon.setVisible(true);
        });

        spnResume.setOnMouseClicked(mouseEvent -> {
            spnResume.setVisible(false);
            spnRestart.setVisible(false);
            spnAbandon.setVisible(false);
        });

        spnRestart.setOnMouseClicked(mouseEvent -> {
            try {
                Main.setShowedPage(new PageMining(skyboxType, groundTypes, items, nextLayerIndex));
            } catch (FileNotFoundException | CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });

        spnAbandon.setOnMouseClicked(mouseEvent -> {
            tryToEndGame(true);
        });
    }
}