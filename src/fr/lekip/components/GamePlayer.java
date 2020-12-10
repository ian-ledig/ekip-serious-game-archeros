package fr.lekip.components;

import fr.lekip.pages.PageMining;
import fr.lekip.utils.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Player object used for mining session. It contain informations of the player
 */
public class GamePlayer extends GameGroup {

    private final int BREAKING_DELTA = 30;
    private final int TEXTURE_DELTA = 19;
    private final int TEXTURE_WIDTH = 55;
    private final int TEXTURE_HEIGHT = 98;

    private final GameImage playerTexture = new GameImage(
            new Image(new FileInputStream(Movement.DOWN.getTexturePath())), 0, TEXTURE_DELTA, TEXTURE_WIDTH,
            TEXTURE_HEIGHT, true);
    private final GameImage toolTexture = new GameImage(null, 10, 76, 40, 48, true);

    private final PageMining parent;

    private Movement movements = Movement.DOWN;
    private Tool tool;

    public GamePlayer(PageMining parent) throws FileNotFoundException {
        this.parent = parent;
        setTranslateY(164);
        add(playerTexture);
        add(toolTexture);
    }

    /**
     * decrement the position X of the player
     * @param delta to decrement
     */
    public void decrementX(int delta) {
        setTranslateX(getTranslateX() - delta);
    }

    /**
     * increment the position X of the player
     * @param delta to increment
     */
    public void incrementX(int delta) {
        setTranslateX(getTranslateX() + delta);
    }

    /**
     * decrement the position Y of the player
     * @param delta to decrement
     */
    public void decrementY(int delta) {
        setTranslateY(getTranslateY() + delta);
    }

    /**
     * increment the position Y of the player
     * @param delta to increment
     */
    public void incrementY(int delta) {
        setTranslateY(getTranslateY() - delta);
    }

    /**
     * set the player direction image
     */
    public void updateMovements() {
        try {
            playerTexture.setImage(new Image(new FileInputStream(movements.getTexturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * try to break the ground
     */
    public void tryToBreak() {
        // Check for the target position
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

            // if player is breaking ground below him : decrement his Y position
            while (movements == Movement.DOWN && canPlayerGo(Direction.DOWN))
                decrementY(7);
        }
    }

    /**
     * recursive method that delete the ground around a given index
     * @param defaultIndex the main targeted point
     * @param index the current index of ground box
     * @param groundBox the list of all ground box
     * @param posX x position targeted
     * @param posY y position targeted
     */
    public void deleteGround(int defaultIndex, int index, GameImage[] groundBox, double posX, double posY) {
        if (tool.getStrength() != 7) {
            if (defaultIndex != -1) {
                int nextIndex = getIndexOf(groundBox, posX, posY);
                if (nextIndex != -1) {
                    int groundTypeIndex = 0;
                    int indexBoxToBreak = getIndexOf(groundBox, posX, posY);
                    int nextLayerIndex = parent.getNextLayerIndex();

                    // Get the resistance of target block
                    if (indexBoxToBreak > PageMining.GROUND_BLOCKS_LINE_NUMBER) {
                        if (indexBoxToBreak < nextLayerIndex)
                            groundTypeIndex = 1;
                        else
                            groundTypeIndex = 2;
                    }
                    int resistance = parent.getGroundTypes().get(groundTypeIndex).getResistance();

                    // Break the block if the tool is sufficiently effective
                    if (tool.getStrength() >= resistance) {
                        if (indexBoxToBreak <= PageMining.GROUND_BLOCKS_NUMBER
                                - (PageMining.GROUND_BLOCKS_LINE_NUMBER + 1)) {
                            if (groundBox[indexBoxToBreak].getImage() != null) {
                                groundBox[indexBoxToBreak].setImage(null);
                            }
                        }

                        // Try to break the neighbors
                        if (index < tool.getStrength() - resistance / 2) {
                            if(index == 0){
                                int randomSoundIndex;
                                switch (tool){
                                    case SHOVEL:
                                        randomSoundIndex = (int) (Math.random() * 4);
                                        Sound.SHOVEL.getMediaPlayer(randomSoundIndex).play();
                                        break;
                                    case PICKAXE:
                                        randomSoundIndex = (int) (Math.random() * 3);
                                        Sound.PICKAXE.getMediaPlayer(randomSoundIndex).play();
                                        break;
                                    case DYNAMITER:
                                        Sound.DYNAMITER.getMediaPlayer().play();
                                        break;
                                }
                            }
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

    }

    /**
     * @param groundBox list of all ground box
     * @param posX position X searched
     * @param posY position Y searched
     * @return the index of a ground box by positions
     */
    public int getIndexOf(GameImage[] groundBox, double posX, double posY) {
        int result = -1;

        // for each ground box, try to get the box who match to positions
        for (int i = 0; i < groundBox.length; i++) {
            if (groundBox[i].getX() == posX && groundBox[i].getY() == posY) {
                result = i;
                break;
            }
        }

        return result;
    }

    /**
     * @param groundBox list of all ground box
     * @param posX position X
     * @param posY position Y
     * @return the exact position of a ground box in an array of two element. [0] for X and [1] for Y
     */
    public double[] getBoxPos(GameImage[] groundBox, double posX, double posY) {
        double[] result = { -1, -1 };

        // for each ground box, try to get the exact position of the right box
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

    /**
     * check if the player can go to a direction
     * @param direction direction to go
     * @return if the player can go to the direction
     */
    public boolean canPlayerGo(Direction direction) {
        boolean result = true;
        GameImage[] groundBox = parent.getGroundBox();

        double posX;
        double posY;
        double[] pos;
        int index;

        // Check for a direction
        switch (direction) {
            case DOWN:
                posX = getTranslateX() + TEXTURE_WIDTH / 2;
                posY = getTranslateY() + TEXTURE_HEIGHT + BREAKING_DELTA;
                pos = getBoxPos(groundBox, posX, posY);

                movements = Movement.DOWN;

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

                movements = Movement.UP;

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

                movements = Movement.RIGHT;

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

                movements = Movement.LEFT;

                if (pos[0] != -1) {
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if (groundBox[index].getImage() != null)
                        result = false;
                }
                break;
        }
        // update the player image to match with the direction
        updateMovements();

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

    /**
     * using probe method
     */
    public void probe() {
        double width = 25.0;
        double index = 1.0;
        List<GameImage> probeSignal = new ArrayList<>();

        // Create a shape of signal where items that are inside of this shape will
        // appear on screen
        for (double i = this.getTranslateY() + 50; i < 751; i++) {

            if (i < getTranslateY() + 116) {
                // Init size of the area
                width += 1.0;
                index += 0.7;
            } else {
                // Starting from the player to the ground we create invisible GameImage that we
                // will use to interact with hidden items
                try {
                    GameImage cir;
                    cir = new GameImage(
                            new Image(new FileInputStream("src/assets/textures/pages/mining/probeSignal.png")), 0, 0,
                            width, 2, false);
                    cir.setTranslateX(getTranslateX() + 27 - index);
                    cir.setTranslateY(i);
                    probeSignal.add(cir);
                    parent.add(cir);
                    width += 0.6;
                    index += 0.3;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

        // We play the probe's sound
        Sound.PROBE.getMediaPlayer().play();

        // After 2 seconds of the sound
        PauseTransition waitAudio = new PauseTransition(Duration.seconds(2));
        waitAudio.setOnFinished(event -> {
            // We check which items are in the range of the probe and we show an area to the
            // player where the item should be
            BoxBlur boxBlur = new BoxBlur();
            boxBlur.setWidth(10);
            boxBlur.setHeight(10);
            boxBlur.setIterations(10);
            List<Circle> tempShape = new ArrayList<>();
            for (GameImage signal : probeSignal) {
                for (Pane item : parent.getGroundItems()) {
                    if(item.getChildren().get(0) instanceof GameImage){
                        Image imgItem = ((GameImage) item.getChildren().get(0)).getImage();

                        if (imgItem != null) {
                            if (signal.getBoundsInParent().intersects(item.getBoundsInParent())) {
                                // We create a new circle to show area where is the item
                                Circle cercTemp = new Circle(20);
                                cercTemp.setFill(Color.GREEN);
                                cercTemp.setCenterX(item.getLayoutX() + item.getWidth() / 2);
                                cercTemp.setCenterY(item.getLayoutY() + item.getHeight() / 2);

                                // We blur the circle
                                cercTemp.setEffect(boxBlur);
                                parent.add(cercTemp);
                                tempShape.add(cercTemp);

                                // We make a blinking animation
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), cercTemp);
                                fadeTransition.setFromValue(1.0);
                                fadeTransition.setToValue(0.0);
                                fadeTransition.setCycleCount(Animation.INDEFINITE);
                                fadeTransition.play();
                            }
                        }
                    }
                }
            }

            // After 2 seconds, we remove the shapes that show where are the items &
            // probeSignal
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished((e) -> {
                for (Circle circle : tempShape) {
                    parent.remove(circle);
                }
                for (GameImage signal : probeSignal) {
                    parent.remove(signal);
                }
            });
            delay.play();
        });

        waitAudio.play();

    }
}