package sample;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class MapEventHandler implements EventHandler<MouseEvent> {

    private static final double MIN_FLICK_PIXELS = 5;
    private static final double FLICK_MOVEMENT = 5;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    // private double startDragX;
    // private double startDragY;
    private double lastXPosition;
    private double lastYPosition;

    private Direction lastFlickDirection;

    public MapEventHandler() {
        lastFlickDirection = null;
    }

    public void addMapEventHandler(Group map) {
        map.setOnMousePressed((e) -> {
            // startDragX = e.getSceneX();
            // startDragY = e.getSceneY();
            lastXPosition = e.getSceneX();
            lastYPosition = e.getSceneY();
        });

        map.setOnMouseDragged((e) -> {
            double currentX = e.getSceneX();
            double currentY = e.getSceneY();

            /*
             * Note : On prends la direction du drag de la souris, et en fonction de ca on
             * calcule la nouvelle translation et position de la map en sauvegardant la
             * dernière position pour pouvoir reprendre de là où on est et ne pas
             * "teleporter" la map sur la souris directement.
             */

            // TODO Optimiser pour ne pas avoir de FLICK_MOVEMENT

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
    }

    @Override
    public void handle(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
