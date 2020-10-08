package fr.lekip.inputs;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class MapEventHandler implements EventHandler<MouseEvent> {

    private static final double MIN_FLICK_PIXELS = 5;
    private static final double FLICK_MOVEMENT = 5;

    private double lastXPosition;
    private double lastYPosition;

    private Direction lastFlickDirection;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    public MapEventHandler(Group map) {

        // Save the mouse position
        map.setOnMousePressed((e) -> {
            lastXPosition = e.getSceneX();
            lastYPosition = e.getSceneY();
        });

        map.setOnMouseDragged((e) -> {
            double currentX = e.getSceneX();
            double currentY = e.getSceneY();

            // TODO Optimiser pour ne pas avoir de FLICK_MOVEMENT

            // Detect as "right flick" if the previous flick direction is not right, and the
            // dragged pixels is more than 10
            if (lastFlickDirection != Direction.RIGHT && currentX - lastXPosition > MIN_FLICK_PIXELS) {
                map.setTranslateX(map.getTranslateX() + FLICK_MOVEMENT);
                lastXPosition = currentX;
            }

            // Detect as "left flick" if the previous flick direction is not left, and the
            // dragged pixels is more than -10
            else if (lastFlickDirection != Direction.LEFT && currentX - lastXPosition < -MIN_FLICK_PIXELS) {
                map.setTranslateX(map.getTranslateX() - FLICK_MOVEMENT);
                lastXPosition = currentX;
            }

            else if (lastFlickDirection != Direction.DOWN && currentY - lastYPosition > MIN_FLICK_PIXELS) {
                map.setTranslateY(map.getTranslateY() + FLICK_MOVEMENT);
                lastYPosition = currentY;
            }

            else if (lastFlickDirection != Direction.UP && currentY - lastYPosition < -MIN_FLICK_PIXELS) {
                map.setTranslateY(map.getTranslateY() - FLICK_MOVEMENT);
                lastYPosition = currentY;
            }
        });
    }

    @Override
    public void handle(MouseEvent arg0) {}
}
