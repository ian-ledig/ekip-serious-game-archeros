package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    // private Animation<?> animation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);

        // Test git

        // Creation of map page
        Map newMap = new Map();
        Group map = newMap.getGroup();
        root.getChildren().add(map);

        // Map Event Handler (temporaire)
        MapEventHandler mev = new MapEventHandler();
        mev.addMapEventHandler(map);

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
