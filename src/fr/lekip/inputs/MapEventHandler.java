package fr.lekip.inputs;

import fr.lekip.components.GameGroup;
import javafx.event.Event;
import javafx.scene.Node;

public class MapEventHandler extends GameEventHandler {

    private static final double MIN_FLICK_PIXELS = 5;
    private static final double FLICK_MOVEMENT = 5;

    private double lastXPosition;
    private double lastYPosition;

    private Direction lastFlickDirection;
    private GameGroup grp;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    public MapEventHandler(GameGroup group) {
        super(group);
        grp = group;
    }

    @Override
    public void loadEventHandler(GameGroup group) {
        // We only take the map + all the pins to move
        Node map = group.getChildren().get(0);

        // Save the mouse position
        map.setOnMousePressed((e) -> {
            lastXPosition = e.getSceneX();
            lastYPosition = e.getSceneY();
        });

        map.setOnMouseDragged((e) -> {
            double currentX = e.getSceneX();
            double currentY = e.getSceneY();

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
    public void handle(Event event) {
        //
    }
}
