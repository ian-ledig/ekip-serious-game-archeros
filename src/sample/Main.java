package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static final double MIN_FLICK_PIXELS = 5;
    private static final double FLICK_MOVEMENT = 5;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    private double startDragX;
    private double startDragY;
    private double lastXPosition;
    private double lastYPosition;
    private Direction lastFlickDirection = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);
        Map newMap = new Map();
        Group map = newMap.getGroup();
        root.getChildren().add(map);

        map.setOnMousePressed((e) -> {
            startDragX = e.getSceneX();
            startDragY = e.getSceneY();
            lastXPosition = e.getSceneX();
            lastYPosition = e.getSceneY();
        });

        map.setOnMouseDragged((e) -> {
            double currentX = e.getSceneX();
            double currentY = e.getSceneY();

            Direction direction;
            // Detect as "right flick" if the previous flick direction is not right, and the
            // dragged pixels is more than 10
            if (lastFlickDirection != Direction.RIGHT && currentX - lastXPosition > MIN_FLICK_PIXELS) {
                direction = Direction.RIGHT;
                map.setTranslateX(map.getTranslateX() + FLICK_MOVEMENT);
                lastXPosition = currentX;
            }

            // Detect as "left flick" if the previous flick direction is not left, and the
            // dragged pixels is more than -10
            else if (lastFlickDirection != Direction.LEFT && currentX - lastXPosition < -MIN_FLICK_PIXELS) {
                direction = Direction.LEFT;
                map.setTranslateX(map.getTranslateX() - FLICK_MOVEMENT);
                lastXPosition = currentX;
            }

            else if (lastFlickDirection != Direction.DOWN && currentY - lastYPosition > MIN_FLICK_PIXELS) {
                direction = Direction.DOWN;
                map.setTranslateY(map.getTranslateY() + FLICK_MOVEMENT);
                lastYPosition = currentY;
            }

            else if (lastFlickDirection != Direction.UP && currentY - lastYPosition < -MIN_FLICK_PIXELS) {
                direction = Direction.UP;
                map.setTranslateY(map.getTranslateY() - FLICK_MOVEMENT);
                lastYPosition = currentY;
            }
            // event.setDragDetect(true);
            // newMap.move(event.getX(), event.getY());
        });

        map.setOnMouseReleased(e -> {
            lastFlickDirection = null;
        });

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
