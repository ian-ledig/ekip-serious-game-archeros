package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.inputs.MapEventHandler;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PageMap extends GameGroup {

    private final Image WORLD_MAP;
    private final Image WORLD_PIN;
    private final VBox sideMenu;
    private final Pane mapp;

    public PageMap() throws FileNotFoundException {
        WORLD_MAP = new Image(new FileInputStream("src/assets/images/worldMap.png"));
        WORLD_PIN = new Image(new FileInputStream("src/assets/images/pin.png"));

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

        // Add map event handler
        addEventHandler(MapEventHandler.class);
    }

    /**
     * Adding the "wait for player" text
     */
    public void loadText() {
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

            GameImage imgPin = new GameImage(WORLD_PIN, 150, 150, 80, 80, true);
            imgPin.setOnMouseClicked(mouseEvent -> {
                System.out.println("Pin cliqué");
            });

            // add(imgPin);
            mapp.getChildren().addAll(imgPin);

            // ici on load le menu aussi <---
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
            Button btnCongo = new Button("Le congo");
            Button btnPyramide = new Button("La Pyramide \nDe Gisee");
            sideMenu.getChildren().add(btnCongo);
            sideMenu.getChildren().add(btnPyramide);

            // Delete event
            setOnMouseClicked(null);
        });
    }
}
