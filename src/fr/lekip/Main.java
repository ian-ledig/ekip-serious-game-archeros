package fr.lekip;

import fr.lekip.components.GameGroup;
import fr.lekip.pages.PageMap;
import fr.lekip.pages.PageMining;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import fr.lekip.utils.GroundType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static int WINDOWS_WIDTH = 1450;
    public static int WINDOWS_HEIGHT = 750;

    public static GameGroup root = new GameGroup();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Scene scene = new Scene(root, WINDOWS_WIDTH, WINDOWS_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        // Set background image
        ImagePattern pattern = new ImagePattern(new Image(new FileInputStream("src/assets/textures/pages/brick.png")));
        scene.setFill(pattern);

        // Page creation
        // Page Mining
        List<GroundType> groundTypes = new ArrayList<>();
        groundTypes.add(GroundType.DIRT);
        groundTypes.add(GroundType.STONE);
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(Item.ARTEFACT01);
        }
        setShowedPage(new PageMining(SkyboxType.BLUE_SKY_CLOUDS, groundTypes, items));

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void setShowedPage(GameGroup showedPage) {
        Main.root.getChildren().clear();
        Main.root.add(showedPage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
