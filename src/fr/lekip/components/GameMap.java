package fr.lekip.components;

import fr.lekip.inputs.MapEventHandler;
import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameMap extends GamePanel {

    private final Image WORLD_MAP;
    private final Image WORLD_PIN;

    public GameMap() throws FileNotFoundException {
        WORLD_MAP = new Image(new FileInputStream("src/assets/images/worldMap.png"));
        WORLD_PIN = new Image(new FileInputStream("src/assets/images/pin.png"));

        // Create the map
        GameImage image = new GameImage(WORLD_MAP, 0, 0, 1400, 958, true);
        add(image);

        // Display text while waiting for the user
        loadText();

        // Enabling map event handler
        new MapEventHandler(this);
    }

    public void loadText() {
        // Create text
        Text text = new Text("Cliquez pour commencer...");
        text.setTranslateX(250);
        text.setTranslateY(250);
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

            add(imgPin);

            // ici on load le menu aussi <---

            // Delete event
            setOnMouseClicked(null);
        });
    }
}
