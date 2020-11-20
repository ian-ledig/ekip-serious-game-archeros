package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.inputs.MapEventHandler;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PageMap extends GameGroup {

    private final Image WORLD_MAP;
    private final Image WORLD_PIN;
    private final VBox sideMenu;
    private final Pane mapp;
    private Object[][] pinCombo;

    public PageMap() throws FileNotFoundException {
        WORLD_MAP = new Image(new FileInputStream("src/assets/textures/pages/main/worldMap.png"));
        WORLD_PIN = new Image(new FileInputStream("src/assets/textures/pages/main/pin.png"));

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

            // We load the menu
            loadMenu();

            try {
                loadIntro();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            // Delete event
            setOnMouseClicked(null);
        });
    }

    private void loadPin() {

        // Each pin
        pinCombo[0][0] = new GameImage(WORLD_PIN, 150, 150, 80, 80, true);
        pinCombo[0][1] = "L'alaska";
        pinCombo[0][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout\nRetour à la ligne test\n\nSaut de ligne test ";
        pinCombo[1][0] = new GameImage(WORLD_PIN, 240, 750, 80, 80, true);
        pinCombo[1][1] = "La tombe sacré";
        pinCombo[1][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout ";
        pinCombo[2][0] = new GameImage(WORLD_PIN, 800, 230, 80, 80, true);
        pinCombo[2][1] = "L'inconnu";
        pinCombo[2][3] = "Description du lieu lalalalalalalalalalalala c 'est bo et c grand et tout ";

        for (int i = 0; i < 3; i++) {
            ((GameImage) pinCombo[i][0]).setOnMouseClicked(mouseEvent -> {
                locationPreview((GameImage) mouseEvent.getSource());
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

            final double tX = ((GameImage) pinCombo[0][0]).getXImage() + 350;
            final double tY = ((GameImage) pinCombo[0][0]).getYImage() + 75;

            TranslateTransition tt = new TranslateTransition();
            tt.setNode(mapp);
            tt.setFromX(mapp.getTranslateX());
            tt.setFromY(mapp.getTranslateY());
            tt.setToX(tX);
            tt.setToY(tY);
            tt.setDuration(new Duration(1500));
            tt.setCycleCount(1);
            tt.setAutoReverse(true);
            tt.play();

        });

        ((Node) pinCombo[1][2]).setOnMouseClicked((e) -> {

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

        });

        ((Node) pinCombo[2][2]).setOnMouseClicked((e) -> {

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

    private void locationPreview(GameImage pinCombo2) {
        // TODO Add specialist choice

        int index = -1;
        for (int i = 0; i < pinCombo.length; i++) {
            if (pinCombo[i][0] == pinCombo2) {
                index = i;
            }
        }

        // Pane + Background color
        Pane pane = new Pane();
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
            GameImage validate = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/main/fouiller.png")), 970, 500, 200, 80,
                    true);

            GameImage landscape = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/main/brora.png")), 10, 10, 500, 225, true);
            pane.getChildren().add(landscape);
            pane.getChildren().add(crossClose);
            pane.getChildren().add(validate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Text description = new Text((String) pinCombo[index][3]);
        description.setTranslateX(500);
        description.setTranslateY(25);
        pane.getChildren().add(description);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setTranslateX(100);
        scrollPane.setTranslateY(100);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(pane);
        scrollPane.setPrefSize(1250, 550);

        // Blur effect in the background
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(8);
        boxBlur.setHeight(4);
        boxBlur.setIterations(2);

        // Add blur effect to all the nodes
        for (Node objects : super.getChildren()) {
            objects.setEffect(boxBlur);
        }
        add(scrollPane);

        getChildren().get(0).setOnMouseDragged(null);
        pane.requestFocus();

        // Add event handler for cross click
        crossClose.setOnMouseClicked((e) -> {
            remove(scrollPane);
            addEventHandler(MapEventHandler.class);
            for (Node objects : super.getChildren()) {
                objects.setEffect(null);
            }
        });

    }
}
