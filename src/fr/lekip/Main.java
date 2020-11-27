package fr.lekip;

import fr.lekip.components.GameGroup;
import fr.lekip.pages.PageMining;
import fr.lekip.pages.SkyboxType;
import fr.lekip.utils.GroundType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static int WINDOWS_WIDTH = 1450;
    public static int WINDOWS_HEIGHT = 750;

    public static GameGroup showedPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Group root = new Group();
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
        setShowedPage(new PageMining(SkyboxType.BLUE_SKY_CLOUDS, groundTypes));

        //setShowedPage(new PageMap());

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
