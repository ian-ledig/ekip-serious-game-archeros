package fr.lekip;

import fr.lekip.components.GameGroup;
import fr.lekip.pages.PageMap;
import fr.lekip.pages.PageMining;
import fr.lekip.utils.GroundType;
import fr.lekip.utils.Item;
import fr.lekip.utils.SkyboxType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static int WINDOWS_WIDTH = 1450;
    public static int WINDOWS_HEIGHT = 750;

    public static MediaPlayer mediaPlayer;
    public static GameGroup root = new GameGroup();

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setup scene
        scene = new Scene(root, WINDOWS_WIDTH, WINDOWS_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        // Page creation
        // Page Mining
        List<GroundType> groundTypes = new ArrayList<>();
        groundTypes.add(GroundType.SAND);
        groundTypes.add(GroundType.SANDSTONE);
        groundTypes.add(GroundType.STONE);
        List<Item> items = new ArrayList<>();
        items.add(Item.COIN);
        items.add(Item.BUTTON);
        items.add(Item.PRIEST);
        items.add(Item.NAIL);
        // setShowedPage(new PageMining(SkyboxType.BLUE_SKY_CLOUDS, groundTypes, items,
        // 900, true));

        setShowedPage(new PageMap(false));
        primaryStage.setTitle("Archeroes");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(new FileInputStream("src/assets/textures/icon.png")));
        primaryStage.show();
    }

    public static void setShowedPage(GameGroup showedPage) {
        Main.root.getChildren().clear();

        if (showedPage instanceof PageMap)
            Main.scene.setFill(Color.web("86B4E4"));
        else {
            try {
                Main.scene.setFill(
                        new ImagePattern(new Image(new FileInputStream("src/assets/textures/pages/brick.png"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Main.root.add(showedPage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
