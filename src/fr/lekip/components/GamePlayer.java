package fr.lekip.components;

import fr.lekip.pages.PageMining;
import fr.lekip.utils.Direction;
import fr.lekip.utils.Movement;
import fr.lekip.utils.Tool;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class GamePlayer extends GameGroup {

    private final int BREAKING_DELTA = 30;
    private final int TEXTURE_DELTA = 19;
    private final int TEXTURE_WIDTH = 55;
    private final int TEXTURE_HEIGHT = 98;
    private GameImage playerTexture = new GameImage(new Image(new FileInputStream(Movement.DOWN.getTexturePath())), 0,
            TEXTURE_DELTA, TEXTURE_WIDTH, TEXTURE_HEIGHT, true);
    private GameImage toolTexture = new GameImage(null, 10, 48, 40, 48, true);

    private Movement movements = Movement.DOWN;
    private Tool tool;

    private PageMining parent;

    public GamePlayer(PageMining parent) throws FileNotFoundException {
        this.parent = parent;
        setTranslateY(164);
        add(playerTexture);
        add(toolTexture);
    }

    public void decrementX(int delta) {
        setTranslateX(getTranslateX() - delta);
        movements = Movement.LEFT;
        updateMovements();
    }

    public void incrementX(int delta) {
        setTranslateX(getTranslateX() + delta);
        movements = Movement.RIGHT;
        updateMovements();
    }

    public void decrementY(int delta) {
        setTranslateY(getTranslateY() + delta);
    }

    public void incrementY(int delta) {
        setTranslateY(getTranslateY() - delta);
    }

    public void updateMovements() {
        try {
            playerTexture.setImage(new Image(new FileInputStream(movements.getTexturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tryToBreak() {
        if (tool != null) {
            double breakPositionX = 0;
            double breakPositionY = 0;
            switch (movements) {
                case UP:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH / 2;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT - BREAKING_DELTA;
                    break;
                case DOWN:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH / 2;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT + BREAKING_DELTA;
                    break;
                case LEFT:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH / 2 - BREAKING_DELTA;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT;
                    break;
                case RIGHT:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH / 2 + BREAKING_DELTA;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT;
                    break;
            }

            // Updating ground
            GameImage[] groundBox = parent.getGroundBox();
            double pos[] = getBoxPos(groundBox, breakPositionX, breakPositionY);
            deleteGround(getIndexOf(groundBox, pos[0], pos[1]), 0, groundBox, pos[0], pos[1]);
            parent.setGroundBox(groundBox);

            while (canPlayerGo(Direction.DOWN) && movements == Movement.DOWN)
                decrementY(7);
        }
    }

    public void deleteGround(int defaultIndex, int index, GameImage[] groundBox, double posX, double posY) {
        if (tool.getStrength() == 7) {

        } else {
            if (defaultIndex != -1) {
                int nextIndex = getIndexOf(groundBox, posX, posY);
                if (nextIndex != -1) {
                    int indexBoxToBreak = getIndexOf(groundBox, posX, posY);
                    if (indexBoxToBreak <= PageMining.GROUND_BLOCKS_NUMBER
                            - (PageMining.GROUND_BLOCKS_LINE_NUMBER + 1)) {
                        if (groundBox[indexBoxToBreak].getImage() != null)
                            groundBox[indexBoxToBreak].setImage(null);
                    }

                    if (index < tool.getStrength()) {
                        index++;
                        deleteGround(defaultIndex, index, groundBox, posX - 18, posY);
                        deleteGround(defaultIndex, index, groundBox, posX + 18, posY);
                        deleteGround(defaultIndex, index, groundBox, posX, posY - 18);
                        deleteGround(defaultIndex, index, groundBox, posX, posY + 18);
                    }
                }
            }
        }

    }

    public int getIndexOf(GameImage[] groundBox, double posX, double posY) {
        int result = -1;

        for (int i = 0; i < groundBox.length; i++) {
            if (groundBox[i].getX() == posX && groundBox[i].getY() == posY) {
                result = i;
                break;
            }
        }

        return result;
    }

    public double[] getBoxPos(GameImage[] groundBox, double posX, double posY) {
        double[] result = { -1, -1 };

        for (int i = 0; i < groundBox.length; i++) {
            double boxPosX = groundBox[i].getX();
            double boxPosY = groundBox[i].getY();
            if (posX >= boxPosX && posX <= boxPosX + 18 && posY >= boxPosY && posY <= boxPosY + 18) {
                result[0] = boxPosX;
                result[1] = boxPosY;
                break;
            }
        }

        return result;
    }

    public boolean canPlayerGo(Direction direction) {
        boolean result = true;
        GameImage[] groundBox = parent.getGroundBox();

        double posX;
        double posY;
        double[] pos;
        int index;

        switch (direction) {
            case DOWN:
                posX = getTranslateX() + TEXTURE_WIDTH / 2;
                posY = getTranslateY() + TEXTURE_HEIGHT + BREAKING_DELTA;
                pos = getBoxPos(groundBox, posX, posY);

                if (pos[0] != -1) {
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if (groundBox[index].getImage() != null
                            || index > PageMining.GROUND_BLOCKS_NUMBER - (PageMining.GROUND_BLOCKS_LINE_NUMBER + 1))
                        result = false;
                }
                break;
            case UP:
                posX = getTranslateX() + TEXTURE_WIDTH / 2;
                posY = getTranslateY() + TEXTURE_HEIGHT - BREAKING_DELTA;
                pos = getBoxPos(groundBox, posX, posY);

                if (pos[0] != -1) {
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if (groundBox[index].getImage() != null)
                        result = false;
                } else if (posY < 240)
                    result = false;
                break;
            case RIGHT:
                posX = getTranslateX() + TEXTURE_WIDTH / 2 + BREAKING_DELTA;
                posY = getTranslateY() + TEXTURE_HEIGHT;
                pos = getBoxPos(groundBox, posX, posY);

                if (pos[0] != -1) {
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if (groundBox[index].getImage() != null)
                        result = false;
                }
                break;
            case LEFT:
                posX = getTranslateX() + TEXTURE_WIDTH / 2 - BREAKING_DELTA;
                posY = getTranslateY() + TEXTURE_HEIGHT;
                pos = getBoxPos(groundBox, posX, posY);

                if (pos[0] != -1) {
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if (groundBox[index].getImage() != null)
                        result = false;
                }
                break;
        }

        return result;
    }

    public GameImage getPlayerTexture() {
        return playerTexture;
    }

    public Movement getMovements() {
        return movements;
    }

    public void setMovements(Movement movements) {
        this.movements = movements;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
        try {
            toolTexture.setImage(new Image(new FileInputStream(tool.getTexturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void probe() {

        double width = 25.0;
        double index = 1.0;
        List<GameImage> probeSignal = new ArrayList<>();

        // Create a shape of signal where items that are inside of this shape will
        // appear on screen
        for (double i = playerTexture.getYImage(); i < 751; i++) {

            // Starting from the player to the ground we create invisible GameImage that we
            // will use to interact with hidden items
            try {
                GameImage cir;
                cir = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/mining/probeSignal.png")),
                        0, 0, width, 2, false);
                cir.setTranslateX(getTranslateX() + 27 - index);
                cir.setTranslateY(i);
                probeSignal.add(cir);
                parent.add(cir);
                width += 0.6;
                index += 0.3;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // We check which items are in the range of the probe and we show an area to the
        // player where the item should be
        List<Circle> tempShape = new ArrayList<>();
        for (GameImage signal : probeSignal) {
            for (GameImage item : parent.getGroundItems()) {
                if (signal.getBoundsInParent().intersects(item.getBoundsInParent())) {
                    Circle cercTemp = new Circle(20);
                    cercTemp.setFill(Color.GREEN);
                    cercTemp.setCenterX(item.getXImage());
                    cercTemp.setCenterY(item.getYImage());
                    parent.add(cercTemp);
                    tempShape.add(cercTemp);

                }
            }
        }

        // After 2 seconds, we remove the shapes that show where are the items
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            for (Circle circle : tempShape) {
                parent.remove(circle);
            }
        });
        delay.play();

    }
}