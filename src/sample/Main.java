package sample;

import javafx.util.Duration;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.*;

import javafx.animation.*;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    // private Animation<?> animation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);

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
