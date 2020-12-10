package fr.lekip.pages;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
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
    private final GameImage btnTutorial;

    private final List<Item> itemsFound = new ArrayList<>();
    private final List<Item> itemsLost = new ArrayList<>();

    private final GamePlayer player = new GamePlayer(this);

    private boolean intro;
    private final int initScore;

    private int itemsRemaining;

    private List<Pane> groundItems = new ArrayList<>();
    private GameImage groundItemWin;
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private ProgressBar energyBar = new ProgressBar();

    private double energyValue = 0;
    private double energyDefault = 0;

    private Label scoreMandatory;
    private Label score;
    private int energyMaxScore;
    private VBox vbxPause;

    private boolean isInPause = false;

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes, List<Item> items, int nextLayerIndex,
            boolean pIntro, int pInitScore) throws FileNotFoundException, CloneNotSupportedException {
        FONT = Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 27.0);

        this.skyboxType = skyboxType;
        this.groundTypes = groundTypes;
        this.items = items;
        this.nextLayerIndex = nextLayerIndex;
        this.btnResume = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/mining/btn.png")), 0, 0,
                150, 30, true);
        this.btnRestart = (GameImage) btnResume.clone();
        this.btnAbandon = (GameImage) btnResume.clone();
        this.btnTutorial = (GameImage) btnResume.clone();

        intro = pIntro;
        initScore = pInitScore;

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
                        for (Pane pneItem : groundItems) {
                            if (spawnPosX + itemSize >= pneItem.getLayoutX()
                                    && spawnPosX <= pneItem.getLayoutX() + pneItem.getWidth()
                                    || spawnPosY + itemSize >= pneItem.getLayoutY()
                                            && spawnPosY <= pneItem.getLayoutY() + pneItem.getWidth()
                                    || Math.abs(Math.sqrt(Math.pow(spawnPosX - pneItem.getLayoutX(), 2)
                                            + Math.pow(spawnPosY - pneItem.getLayoutY(), 2))) < 200) {
                                correctPos = false;
                                break;
                            }
                        }
                    }
                }

                // Add and position the item
                Pane pneItem = new Pane();
                GameImage newItem = item.cloneGameImage();
                pneItem.setLayoutX(spawnPosX);
                pneItem.setLayoutY(spawnPosY);
                pneItem.getChildren().add(newItem);
                add(pneItem);

                // Try to set item to mandatory object
                if (!itemWinAssigned && (item == items.get(items.size() - 1) || (int) (Math.random() * 3) == 0)) {
                    itemWinAssigned = true;
                    groundItemWin = newItem;
                    itemWin = item;
                }
                groundItems.add(pneItem);

                // Delete the item and increase the number of objects found when item is clicked
                pneItem.setOnMouseClicked(mouseEvent -> {
                    if (newItem.getImage() != null) {

                        Tool tool = player.getTool();

                        // If the used tool is strong enough
                        // Try to add the item to the founded or lost lists
                        if (tool != null && tool.getStrength() >= item.getMinResistance()) {
                            if (tool.getStrength() <= item.getMaxResistance()) {
                                this.itemsFound.add(item);
                                Sound.PICK_UP.getMediaPlayer().play();
                            } else {
                                this.itemsLost.add(item);
                                itemsRemaining--;
                                score.setTextFill(Color.INDIANRED);
                                Sound.BREAK.getMediaPlayer().play();
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
            energyMaxScore = 0;
            for (int i = 0; i < groundItems.size(); i++) {
                energyDefault += (getGroundItems().get(i).getLayoutX() - 262) * 1.2 + 7;
                energyMaxScore += (getGroundItems().get(i).getLayoutY() - 262);

                // We take the deepest item Y
                if ((getGroundItems().get(i).getLayoutY() - 262) > Y1) {
                    Y1 = (getGroundItems().get(i).getLayoutY() - 262);
                }

                // We save the first item in X basis and the last item
                if ((getGroundItems().get(i).getLayoutX()) < xG) {
                    xG = getGroundItems().get(i).getLayoutX();
                }
                if ((getGroundItems().get(i).getLayoutX()) > xD) {
                    xD = getGroundItems().get(i).getLayoutX();
                }

            }
            energyDefault += (xD - xG);

            // We calculate the malus
            double malus;
            malus = Y1 * 3;
            for (Pane item : getGroundItems()) {
                if (item.getLayoutY() != Y1) {
                    malus -= item.getLayoutY();
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

        // Play game music
        Main.mediaPlayer = Sound.GAME.getMediaPlayer();
        Main.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        Main.mediaPlayer.play();
        if (intro) {
            loadTutorial();
        } else {
            // Add player movements event handler
            addEventHandler(PlayerMovementsEventHandler.class);
        }

    }

    public void loadTutorial() throws FileNotFoundException {
        StackPane tuto = new StackPane();
        tuto.setPrefSize(1450, 750);

        GameImage backgroundTuto = new GameImage(
                new Image(new FileInputStream("src/assets/textures/pages/mining/tutoBack.png")), 0, 0, 1000, 615,
                false);
        tuto.getChildren().add(backgroundTuto);
        tuto.setAlignment(Pos.CENTER);

        add(tuto);

        Text title = new Text("TUTORIEL");
        title.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/coco_gothic/CocoGothic_trial.ttf")), 18.0));
        title.setFill(Color.ORANGE);
        title.setX(685);
        title.setY(100);

        Pane pnIntro = new Pane();
        Label txtIntro = new Label(
                "        Vous êtes arrivé à votre lieu de fouille. Il vous faut à présent fouiller pour trouver l'objectif que vous recherchez ! Pour cela, une sonde \nainsi que des outils de minage sont à votre dispotion. Leur utilisation n'est cependant pas gratuite ; ce sera à vous de les utiliser avec \nprécaution ! Chaque lieu cache un total de 4 items à récupérer. Mais garde à vous ; seul l'item objectif est nécessaire pour réussir votre fouille !");
        txtIntro.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
        txtIntro.setTextFill(Color.WHITE);
        txtIntro.setWrapText(true);
        txtIntro.maxWidth(100);
        txtIntro.setTranslateX(10);
        pnIntro.getChildren().add(txtIntro);
        pnIntro.setTranslateX(250);
        pnIntro.setTranslateY(150);

        Pane pnOutils = new Pane();
        Label txtOutils = new Label(
                "Force et coût des outils :\n        : 1                                                                                      : 2\n\n        : 4                                                                                      : 0 - 7");
        txtOutils.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
        txtOutils.setTextFill(Color.WHITE);
        txtOutils.setWrapText(true);
        txtOutils.maxWidth(100);
        txtOutils.setTranslateX(10);
        pnOutils.getChildren().add(txtOutils);
        pnOutils.setTranslateX(250);
        pnOutils.setTranslateY(270);

        GameImage shovel = new GameImage(new Image(new FileInputStream("src/assets/textures/tools/shovel.png")), 250,
                290, 30, 30, true);
        GameImage pickaxe = new GameImage(new Image(new FileInputStream("src/assets/textures/tools/pickaxe.png")), 620,
                290, 30, 30, true);
        GameImage dynamite = new GameImage(new Image(new FileInputStream("src/assets/textures/tools/dynamiter.png")),
                250, 335, 30, 30, true);
        GameImage probe = new GameImage(new Image(new FileInputStream("src/assets/textures/tools/probe.png")), 620, 335,
                30, 30, true);

        Label movement = new Label(
                "Commandes : \nZ = Se déplacer vers le haut\nQ = Se déplacer à gauche\nS = Se déplacer vers le bas\nD = Se déplacer à droite\nF = Changer d'outil\nClique gauche souris = Utiliser outil\nPour miner, indiquer la direction avec ZQSD avant de cliquer !");
        movement.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
        movement.setTextFill(Color.WHITE);
        movement.setTranslateX(250);
        movement.setTranslateY(390);

        Pane pnNrg = new Pane();
        GameImage nrgImage = new GameImage(
                new Image(new FileInputStream("src/assets/textures/pages/mining/tutoEnergy.png")), 0, 0, 200, 100,
                true);
        Label txtNrg = new Label(
                "        Vous avez une énergie limitée qui vous permettra de casser des blocs et d'utiliser vos outils. Lorsque votre énergie tombe à 0, la partie se termine. Votre energie totale est déterminée selon la difficulté de la fouille.");
        txtNrg.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
        txtNrg.setTextFill(Color.WHITE);
        txtNrg.setWrapText(true);
        txtNrg.setMaxWidth(700);
        txtNrg.setTranslateX(220);

        pnNrg.getChildren().addAll(nrgImage, txtNrg);
        pnNrg.setTranslateX(250);
        pnNrg.setTranslateY(600);

        StackPane btnValidate = new StackPane();
        GameImage validate = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/mining/btn.png")),
                0, 0, 150, 30, true);
        Text txtValidate = new Text("Compris");
        txtValidate.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
        btnValidate.getChildren().add(validate);
        btnValidate.getChildren().add(txtValidate);
        btnValidate.setAlignment(Pos.CENTER);
        btnValidate.setTranslateX(650);
        btnValidate.setTranslateY(700);

        add(shovel);
        add(pickaxe);
        add(dynamite);
        add(probe);
        add(movement);
        add(title);
        add(pnIntro);
        add(pnOutils);
        add(pnNrg);
        add(btnValidate);

        intro = true;

        btnValidate.setOnMouseClicked((e) -> {
            remove(tuto);
            remove(movement);
            remove(btnValidate);
            remove(title);
            remove(pnIntro);
            remove(pnNrg);
            remove(pnOutils);
            remove(shovel);
            remove(pickaxe);
            remove(dynamite);
            remove(probe);
            addEventHandler(PlayerMovementsEventHandler.class);
            isInPause = false;
            intro = false;
        });

    }

    public void tryToEndGame(boolean force) {
        if (force || isEnd()) {
            Main.mediaPlayer.stop();
            if(itemsFound.contains(itemWin))
                Sound.WIN.getMediaPlayer().play();
            else
                Sound.LOSE.getMediaPlayer().play();
 PageSummary summary = new PageSummary(itemsFound, itemsLost, itemWin, energyBar.getProgress(), initScore,
                    energyMaxScore);
            setOnKeyPressed(null);
            setOnMouseClicked(null);
            setOnMousePressed(null);
            add(summary);

            summary.start();
        }
    }

    public boolean isEnd() {
        return groundItemWin == null && !itemsFound.contains(itemWin)
                || itemsFound.size() + itemsLost.size() == groundItems.size() || energyBar.getProgress() <= 0;
    }

    public List<Pane> getGroundItems() {
        return groundItems;
    }

    public void setGroundItems(List<Pane> groundItems) {
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

    public boolean isIntro() {
        return intro;
    }

    public boolean isInPause() {
        return isInPause;
    }

    public void switchPause() {
        isInPause = !isInPause;
        Sound.BUTTON.getMediaPlayer().play();
        vbxPause.setVisible(!vbxPause.isVisible());
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
            if(!isInPause && !intro){
                Tool tool = player.getTool();
                if (tool != null)
                    decreaseEnergy(player.getTool().getStrength());
            }
        });

        // Calculation of the maximal energy
        //
    }

    public void decreaseEnergy(int strength) {
        energyValue -= strength * 18;
        energyBar.setProgress(((energyValue * 100) / energyDefault) * 0.01);
        tryToEndGame(false);
    }

    public void loadLabels() throws FileNotFoundException {
        scoreMandatory = new Label("Objectif : ");
        scoreMandatory.setFont(FONT);
        HBox hbxScoreMand = new HBox(20);
        hbxScoreMand.setTranslateX(1288);
        hbxScoreMand.setTranslateY(71);
        hbxScoreMand.setSpacing(5);
        hbxScoreMand.getChildren().add(scoreMandatory);
        add(hbxScoreMand);

        try {
            GameImage imgItemWin = itemWin.cloneGameImage();
            imgItemWin.setLayoutX(1378);
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

        StackPane spnTutorial = new StackPane();
        Text txtTutorial = new Text("Tutoriel");
        txtTutorial.setFont(FONT);
        spnTutorial.setAlignment(Pos.CENTER);
        spnTutorial.getChildren().addAll(btnTutorial, txtTutorial);
        add(spnTutorial);

        vbxPause = new VBox();
        vbxPause.setPrefWidth(150);
        vbxPause.setTranslateX(650);
        vbxPause.setTranslateY(300);
        vbxPause.setSpacing(25);
        vbxPause.getChildren().addAll(spnResume, spnRestart, spnTutorial, spnAbandon);
        vbxPause.setVisible(false);
        add(vbxPause);

        spnPause.setOnMouseClicked(event -> {
            switchPause();
        });

        spnResume.setOnMouseClicked(mouseEvent -> {
            switchPause();
        });

        spnRestart.setOnMouseClicked(mouseEvent -> {
            Sound.QUIT.getMediaPlayer().play();
            Main.mediaPlayer.stop();

            try {
                Main.setShowedPage(new PageMining(skyboxType, groundTypes, items, nextLayerIndex, false, initScore));
            } catch (FileNotFoundException | CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });

        spnTutorial.setOnMouseClicked(mouseEvent -> {
            vbxPause.setVisible(!vbxPause.isVisible());
            try {
                loadTutorial();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        spnAbandon.setOnMouseClicked(mouseEvent -> {
            Sound.QUIT.getMediaPlayer().play();;

            tryToEndGame(true);
        });
    }
}