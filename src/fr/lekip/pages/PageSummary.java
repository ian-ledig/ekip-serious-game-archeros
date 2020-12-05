package fr.lekip.pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class PageSummary extends GameGroup {

    public PageSummary() {
        try {

            StackPane pane = new StackPane();
            GameImage image = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/summary/end_screen.png")), 1450 / 4,
                    750 / 4, 1150, 590, true);
            pane.getChildren().add(image);
            // alignemnt
            add(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
