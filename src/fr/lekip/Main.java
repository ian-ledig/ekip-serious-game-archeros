package fr.lekip;

import java.io.FileInputStream;

import fr.lekip.components.GameGroup;
import fr.lekip.pages.PageMap;
import fr.lekip.pages.PageMining;
import fr.lekip.pages.SkyboxType;
import fr.lekip.utils.GroundType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static GameGroup showedPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Group root = new Group();
        Scene scene = new Scene(root, 1450, 750);
        scene.setFill(Color.TRANSPARENT);

        // Set background image
        ImagePattern pattern = new ImagePattern(new Image(new FileInputStream("src/assets/images/brick2.png")));
        scene.setFill(pattern);
        // Page creation

        // Page Mining
        // List<GroundType> groundTypes = new ArrayList<>();
        // groundTypes.add(GroundType.DIRT);
        // groundTypes.add(GroundType.STONE);
        // setShowedPage(new PageMining(SkyboxType.BLUE_SKY_CLOUDS, groundTypes));

        setShowedPage(new PageMap());

        root.getChildren().add(showedPage);

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void setShowedPage(GameGroup showedPage) {
        Main.showedPage = showedPage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
