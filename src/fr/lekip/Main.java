package fr.lekip;

import fr.lekip.components.GameMap;
import fr.lekip.components.GamePanel;
import fr.lekip.inputs.MapEventHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static GamePanel showedPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);

        // Page creation
        setShowedPage(new GameMap());
        root.getChildren().add(showedPage);

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setShowedPage(GamePanel showedPage) {
        Main.showedPage = showedPage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
