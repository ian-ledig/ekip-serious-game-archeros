package fr.lekip;

import fr.lekip.components.Map;
import fr.lekip.inputs.MapEventHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);

        // Page creation
        Group map = new Map();
        root.getChildren().add(map);

        // Map Event Handler
        MapEventHandler event = new MapEventHandler(map);

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
