package fr.lekip.pages;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GameSpecialist;
import fr.lekip.inputs.MapEventHandler;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import fr.lekip.utils.GroundType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PageMap extends GameGroup {

    private final Image WORLD_MAP;
    private final Image WORLD_PIN;
    private final VBox sideMenu;
    private final Pane mapp;
    private Object tempView;
    private Object[][] pinCombo;
    private boolean intro;
    private int index;
    private GameImage validate;
    private Pane pane;
    private Random rand = new Random();

    private Item[][] locationItems = { { Item.COIN, Item.PRIEST, Item.BUTTON, Item.NAIL }, {}, {} };
    private GroundType[][] locationGround = { { GroundType.SAND, GroundType.SANDSTONE, GroundType.STONE }, {}, {} };

    public PageMap(boolean pIntro) throws FileNotFoundException {
        WORLD_MAP = new Image(new FileInputStream("src/assets/textures/pages/main/worldMap.png"));
        WORLD_PIN = new Image(new FileInputStream("src/assets/textures/pages/main/pin.png"));

        intro = pIntro;
        // pinCombo = new HashMap<GameImage, Button>();
        pinCombo = new Object[3][4];

        // Create the map
        sideMenu = new VBox();
        sideMenu.setTranslateX(1200);

        mapp = new Pane();
        GameImage image = new GameImage(WORLD_MAP, 0, 0, 1700, 1250, true);

        mapp.getChildren().addAll(image);
        add(mapp);
        add(sideMenu);

        // Display text while waiting for the user
        loadText();

    }

    /**
     * Adding the "wait for player" text
     */
    private void loadText() {
        // Create text
        Text text = new Text("Cliquez pour commencer...");
        text.setTranslateX(550);
        text.setTranslateY(375);
        text.setStyle("-fx-fill: purple;-fx-font: 30 arial; ");

        add(text);

        // Create animation
        final ScaleTransition st = new ScaleTransition();
        st.setNode(text);

        // From size
        st.setFromX(1.0);
        st.setFromY(1.0);

        // To size
        st.setToX(1.3);
        st.setToY(1.3);

        st.setDuration(new Duration(1500));
        st.setAutoReverse(true);
        st.setCycleCount(-1);

        st.play();

        // Set mouse clicked event
        setOnMouseClicked((e) -> {
            st.stop();
            remove(text);

            loadPin();

            // ici on load le menu aussi <---
            loadMenu();

            if (intro) {
                try {
                    loadIntro();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            } else {
                // Add map event handler
                addEventHandler(MapEventHandler.class);
            }

            // Delete event
            setOnMouseClicked(null);
        });
    }

    private void loadPin() {

        // Each pin
        pinCombo[0][0] = new GameImage(WORLD_PIN, 645, 98, 80, 80, true);
        pinCombo[0][1] = "Brora";
        pinCombo[0][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout\nRetour à la ligne test\n\nSaut de ligne test ";
        pinCombo[1][0] = new GameImage(WORLD_PIN, 648, 200, 80, 80, true);
        pinCombo[1][1] = "Perth";
        pinCombo[1][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout ";
        pinCombo[2][0] = new GameImage(WORLD_PIN, 761, 211, 80, 80, true);
        pinCombo[2][1] = "Eretrie";
        pinCombo[2][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout ";

        for (int i = 0; i < 3; i++) {
            ((GameImage) pinCombo[i][0]).setOnMouseClicked(mouseEvent -> {
                locationPreview((GameImage) mouseEvent.getSource());
                System.out.println("Pin cliqué");
            });
            mapp.getChildren().addAll((GameImage) pinCombo[i][0]);

        }

    }

    private void loadMenu() {
        Image sand;
        try {
            // loading of background
            sand = new Image(new FileInputStream("src/assets/textures/pages/main/fond.png"));
            BackgroundSize backgroundSize = new BackgroundSize(250, 750, false, false, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(sand, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
            sideMenu.setBackground(new Background(backgroundImage));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        sideMenu.setMinSize(250, 750);
        sideMenu.setMaxSize(250, 750);

        try {
            loadButtons();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadButtons() throws FileNotFoundException {
        int y = 10;
        for (int i = 0; i < 3; i++) {
            // Load every button in the menu
            GameImage btn = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/main/btn.png")), 10,
                    y, 170, 100, true);
            Text btnTxt = new Text((String) pinCombo[i][1]);
            StackPane panel = new StackPane();
            panel.setPadding(new Insets(5, 5, 5, 5));
            panel.getChildren().add(btn);
            panel.getChildren().add(btnTxt);
            panel.setAlignment(Pos.CENTER);
            pinCombo[i][2] = panel;
            sideMenu.getChildren().add((StackPane) pinCombo[i][2]);
            y += 50;
        }

        // TODO to refactor
        // Event handler to move the map in the location clicked
        ((Node) pinCombo[0][2]).setOnMouseClicked((e) -> {


            TranslateTransition tt = new TranslateTransition();
            tt.setNode(mapp);
            tt.setFromX(mapp.getTranslateX());
            tt.setFromY(mapp.getTranslateY());
            tt.setToX(400);
            tt.setToY(800);
            tt.setDuration(new Duration(1500));
            tt.setCycleCount(1);
            tt.setAutoReverse(true);
            

            ScaleTransition transition1 = new ScaleTransition(Duration.seconds(2), mapp);
            transition1.setToX(3);
            transition1.setToY(3);

            tt.play();
            transition1.play();
            

        });

        ((Node) pinCombo[1][2]).setOnMouseClicked((e) -> {

            TranslateTransition tt = new TranslateTransition();
            tt.setNode(mapp);
            tt.setFromX(mapp.getTranslateX());
            tt.setFromY(mapp.getTranslateY());
            tt.setToX(400);
            tt.setToY(600);
            tt.setDuration(new Duration(1500));
            tt.setCycleCount(1);
            tt.setAutoReverse(true);

            ScaleTransition transition1 = new ScaleTransition(Duration.seconds(2), mapp);
            transition1.setToX(3);
            transition1.setToY(3);

            tt.play();
            transition1.play();
            
        });

        ((Node) pinCombo[2][2]).setOnMouseClicked((e) -> {

            TranslateTransition tt = new TranslateTransition();
            tt.setNode(mapp);
            tt.setFromX(mapp.getTranslateX());
            tt.setFromY(mapp.getTranslateY());
            tt.setToX(70);
            tt.setToY(500);
            tt.setDuration(new Duration(1500));
            tt.setCycleCount(1);
            tt.setAutoReverse(true);
            
            ScaleTransition transition1 = new ScaleTransition(Duration.seconds(2), mapp);
            transition1.setToX(3);
            transition1.setToY(3);

            tt.play();
            transition1.play();
        });
    }

    private void locationPreview(GameImage pinCombo2) {
        // TODO Add specialist choice

        index = -1;
        for (int i = 0; i < pinCombo.length; i++) {
            if (pinCombo[i][0] == pinCombo2) {
                index = i;
            }
        }
        System.out.println(pinCombo[index][1]);
        // Pane + Background color
        pane = new Pane();
        try {
            // here we change the size of the background
            GameImage back = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/main/fondettout.png")), 0, 0, 1250, 600,
                    true);
            pane.getChildren().add(back);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        GameImage crossClose = null;
        try {
            crossClose = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/main/cross.png")), 1200,
                    5, 20, 20, true);
            validate = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/main/fouiller.png")), 970,
                    490, 200, 80, true);

            GameImage landscape = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/main/brora.png")), 10, 10, 500, 225, true);
            pane.getChildren().add(landscape);
            pane.getChildren().add(crossClose);
            pane.getChildren().add(validate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Add description of the location
        Text description = new Text((String) pinCombo[index][3]);
        description.setTranslateX(500);
        description.setTranslateY(25);
        pane.getChildren().add(description);

        List<GameSpecialist> specialists = loadSpecialist(index);

        validate.setOnMouseClicked((e) -> {

            int scoreInit = 0;
            for (GameSpecialist spec : specialists) {
                if (spec.getChecked() && spec.getCorrect()) {
                    scoreInit += 25;
                } else if (spec.getChecked() && spec.getCorrect() == false) {
                    scoreInit -= 15;
                }
            }

            List<GroundType> groundTypes = new ArrayList<>();
            List<Item> items = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                items.add(locationItems[index][j]);
                if (j < 3) {
                    groundTypes.add(locationGround[index][j]);
                }
            }

            try {
                Main.setShowedPage(
                        new PageMining(SkyboxType.BLUE_SKY_CLOUDS, groundTypes, items, 900, intro, scoreInit));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // ScrollPane scrollPane = new ScrollPane();
        // scrollPane.setTranslateX(100);
        // scrollPane.setTranslateY(100);
        // scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        // scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        // scrollPane.setContent(pane);
        // scrollPane.setPrefSize(1250, 550);
        pane.setTranslateX(100);
        pane.setTranslateY(100);
        // Blur effect in the background
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(8);
        boxBlur.setHeight(4);
        boxBlur.setIterations(2);

        // Add blur effect to all the nodes
        for (Node objects : super.getChildren()) {
            objects.setEffect(boxBlur);
        }
        add(pane);

        getChildren().get(0).setOnMouseDragged(null);
        pane.requestFocus();

        // Add event handler for cross click
        crossClose.setOnMouseClicked((e) -> {
            remove(pane);
            addEventHandler(MapEventHandler.class);
            for (Node objects : super.getChildren()) {
                objects.setEffect(null);
            }
        });

    }

    private void loadIntro() throws FileNotFoundException {
        GameGroup introPage = new PageIntro();

        // Blur effect in the background
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(8);
        boxBlur.setHeight(4);
        boxBlur.setIterations(2);

        // Add blur effect to all the nodes
        for (Node objects : super.getChildren()) {
            objects.setEffect(boxBlur);
        }
        add(introPage);

        introPage.requestFocus();
        introPage.setOnKeyPressed((e) -> {
            if (((PageIntro) introPage).isFinished()) {
                for (Node objects : super.getChildren()) {
                    objects.setEffect(null);
                }
                remove(introPage);

                // Add map event handler
                addEventHandler(MapEventHandler.class);
            }
        });

    }

    private List<GameSpecialist> loadSpecialist(int index) {

        // Add all the specialist that we can chose
        List<GameSpecialist> specialists = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            GameSpecialist temp = GameSpecialist.getSpecificSpecialist(locationItems[index][i]);

            boolean test = false;
            for (GameSpecialist gameSpecialist : specialists) {
                if (gameSpecialist.toString() == temp.toString()) {
                    test = true;
                }
            }
            if (!test) {
                specialists.add(temp);
            }
        }

        boolean in = true;
        while (in) {

            GameSpecialist temp2 = new GameSpecialist(0, 0, rand.nextInt(6), false);

            boolean test = false;
            for (GameSpecialist gameSpecialist : specialists) {
                if (gameSpecialist.toString() == temp2.toString()) {
                    test = true;
                }
            }
            if (!test) {
                specialists.add(temp2);
                in = false;
            }

        }
        Collections.shuffle(specialists);
        int x = 50;
        for (GameSpecialist gameSpecialist : specialists) {
            gameSpecialist.setPos(x, 250);
            x += 200;
            pane.getChildren().add(gameSpecialist.getSpecialist());
        }
        return specialists;
    }

}
