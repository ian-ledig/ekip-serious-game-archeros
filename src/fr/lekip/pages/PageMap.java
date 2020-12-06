package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.inputs.MapEventHandler;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PageMap extends GameGroup {

    private final Image WORLD_MAP;
    private final Image WORLD_PIN;
    private final VBox sideMenu;
    private final Pane mapp;
    private Object tempView;
    private Object[][] pinCombo;
    private boolean intro;

    public PageMap(boolean pIntro) throws FileNotFoundException {
        WORLD_MAP = new Image(new FileInputStream("src/assets/textures/pages/main/worldMap.png"));
        WORLD_PIN = new Image(new FileInputStream("src/assets/textures/pages/main/pin.png"));

        intro = pIntro;
        // pinCombo = new HashMap<GameImage, Button>();
        pinCombo = new Object[3][3];

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
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            // Delete event
            setOnMouseClicked(null);
        });
    }

    private void loadPin() {

        // Each pin
        pinCombo[0][0] = new GameImage(WORLD_PIN, 150, 150, 80, 80, true);
        pinCombo[0][1] = "L'alaska";
        pinCombo[1][0] = new GameImage(WORLD_PIN, 240, 750, 80, 80, true);
        pinCombo[1][1] = "La tombe sacré";
        pinCombo[2][0] = new GameImage(WORLD_PIN, 800, 230, 80, 80, true);
        pinCombo[2][1] = "L'inconnu";

        for (int i = 0; i < 3; i++) {
            ((GameImage) pinCombo[i][0]).setOnMouseClicked(mouseEvent -> {
                System.out.println("Pin cliqué");
            });
            mapp.getChildren().addAll((GameImage) pinCombo[i][0]);

        }

    }

    private void loadMenu() {
        Image sand;
        try {
            // loading of background
            sand = new Image(new FileInputStream("src/assets/images/Sand.png"));
            BackgroundSize backgroundSize = new BackgroundSize(250, 750, false, false, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(sand, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
            sideMenu.setBackground(new Background(backgroundImage));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        sideMenu.setMinSize(250, 750);
        sideMenu.setMaxSize(250, 750);

        loadButtons();

    }

    private void loadButtons() {

        // TODO to refactor
        ((Button) pinCombo[0][2]).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                final double x = ((GameImage) pinCombo[0][0]).getXImage() + 350;
                final double y = ((GameImage) pinCombo[0][0]).getYImage() + 75;

                TranslateTransition tt = new TranslateTransition();
                tt.setNode(mapp);
                tt.setFromX(mapp.getTranslateX());
                tt.setFromY(mapp.getTranslateY());
                tt.setToX(x);
                tt.setToY(y);
                tt.setDuration(new Duration(1500));
                tt.setCycleCount(1);
                tt.setAutoReverse(true);
                tt.play();
            }
        });

        ((Button) pinCombo[1][2]).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                TranslateTransition tt = new TranslateTransition();
                tt.setNode(mapp);
                tt.setFromX(mapp.getTranslateX());
                tt.setFromY(mapp.getTranslateY());
                tt.setToX(500);
                tt.setToY(-400);
                tt.setDuration(new Duration(1500));
                tt.setCycleCount(1);
                tt.setAutoReverse(true);
                tt.play();
            }
        });

        ((Button) pinCombo[2][2]).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                TranslateTransition tt = new TranslateTransition();
                tt.setNode(mapp);
                tt.setFromX(mapp.getTranslateX());
                tt.setFromY(mapp.getTranslateY());
                tt.setToX(-350);
                tt.setToY(-115);
                tt.setDuration(new Duration(1500));
                tt.setCycleCount(1);
                tt.setAutoReverse(true);
                tt.play();
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

}
