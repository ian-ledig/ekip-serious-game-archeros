package fr.lekip.components;

import fr.lekip.pages.PageMining;
import fr.lekip.utils.*;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GamePlayer extends GameGroup{

    private final int BREAKING_DELTA = 30;
    private final int TEXTURE_DELTA = 19;
    private final int TEXTURE_WIDTH = 55;
    private final int TEXTURE_HEIGHT = 98;

    private final GameImage playerTexture = new GameImage(new Image(new FileInputStream(Movement.DOWN.getTexturePath())), 0, TEXTURE_DELTA, TEXTURE_WIDTH, TEXTURE_HEIGHT, true);
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

    public void decrementX(int delta){
        setTranslateX(getTranslateX() - delta);
    }

    public void incrementX(int delta){
        setTranslateX(getTranslateX() + delta);
    }

    public void decrementY(int delta){
        setTranslateY(getTranslateY() + delta);
    }

    public void incrementY(int delta){
        setTranslateY(getTranslateY() - delta);
    }

    public void updateMovements(){
        try {
            playerTexture.setImage(new Image(new FileInputStream(movements.getTexturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tryToBreak(){
        // Check for the target position
        if(tool != null){
            double breakPositionX = 0;
            double breakPositionY = 0;
            switch (movements){
                case UP:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH/2;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT - BREAKING_DELTA;
                    break;
                case DOWN:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH/2;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT + BREAKING_DELTA;
                    break;
                case LEFT:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH/2 - BREAKING_DELTA;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT;
                    break;
                case RIGHT:
                    breakPositionX = getTranslateX() + TEXTURE_WIDTH/2 + BREAKING_DELTA;
                    breakPositionY = getTranslateY() + TEXTURE_HEIGHT;
                    break;
            }

            // Updating ground
            GameImage[] groundBox = parent.getGroundBox();
            double pos[] = getBoxPos(groundBox, breakPositionX, breakPositionY);
            deleteGround(getIndexOf(groundBox, pos[0], pos[1]), 0, groundBox, pos[0], pos[1]);
            parent.setGroundBox(groundBox);

            while (movements == Movement.DOWN && canPlayerGo(Direction.DOWN))
                decrementY(7);
        }
    }

    public void deleteGround(int defaultIndex, int index, GameImage[] groundBox, double posX, double posY){
        if(defaultIndex != -1){
            int nextIndex = getIndexOf(groundBox, posX, posY);
            if(nextIndex != -1){
                int groundTypeIndex = 0;
                int indexBoxToBreak = getIndexOf(groundBox, posX, posY);
                int nextLayerIndex = parent.getNextLayerIndex();

                // Get the resistance of target block
                if(indexBoxToBreak > PageMining.GROUND_BLOCKS_LINE_NUMBER) {
                    if (indexBoxToBreak < nextLayerIndex)
                        groundTypeIndex = 1;
                    else
                        groundTypeIndex = 2;
                }
                int resistance = parent.getGroundTypes().get(groundTypeIndex).getResistance();

                // Break the block if the tool is sufficiently effective
                if(tool.getStrength() >= resistance){
                    if(indexBoxToBreak <= PageMining.GROUND_BLOCKS_NUMBER - (PageMining.GROUND_BLOCKS_LINE_NUMBER + 1)) {
                        if (groundBox[indexBoxToBreak].getImage() != null){
                            groundBox[indexBoxToBreak].setImage(null);
                        }
                    }

                    // Try to break the neighbors
                    if(index < tool.getStrength() - resistance / 2){
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

    public int getIndexOf(GameImage[] groundBox, double posX, double posY){
        int result = -1;

        for (int i = 0; i < groundBox.length; i++) {
            if(groundBox[i].getX() == posX && groundBox[i].getY() == posY){
                result = i;
                break;
            }
        }

        return result;
    }

    public double[] getBoxPos(GameImage[] groundBox, double posX, double posY){
        double[] result = {-1, -1};

        for (int i = 0; i < groundBox.length; i++) {
            double boxPosX = groundBox[i].getX();
            double boxPosY = groundBox[i].getY();
            if(
                    posX >= boxPosX &&
                    posX <= boxPosX + 18 &&
                    posY >= boxPosY &&
                    posY <= boxPosY + 18
            ){
                result[0] = boxPosX;
                result[1] = boxPosY;
                break;
            }
        }

        return result;
    }

    public boolean canPlayerGo(Direction direction){
        boolean result = true;
        GameImage[] groundBox = parent.getGroundBox();

        double posX;
        double posY;
        double[] pos;
        int index;

        // Check for a direction
        switch (direction){
            case DOWN:
                posX = getTranslateX() + TEXTURE_WIDTH/2;
                posY = getTranslateY() + TEXTURE_HEIGHT + BREAKING_DELTA;
                pos = getBoxPos(groundBox, posX, posY);

                movements = Movement.DOWN;

                if(pos[0] != -1){
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if(groundBox[index].getImage() != null || index > PageMining.GROUND_BLOCKS_NUMBER - (PageMining.GROUND_BLOCKS_LINE_NUMBER + 1))
                        result = false;
                }
                break;
            case UP:
                posX = getTranslateX() + TEXTURE_WIDTH/2;
                posY = getTranslateY() + TEXTURE_HEIGHT - BREAKING_DELTA;
                pos = getBoxPos(groundBox, posX, posY);

                movements = Movement.UP;

                if(pos[0] != -1){
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if(groundBox[index].getImage() != null)
                        result = false;
                }
                else if(posY < 240)
                    result = false;
                break;
            case RIGHT:
                posX = getTranslateX() + TEXTURE_WIDTH/2 + BREAKING_DELTA;
                posY = getTranslateY() + TEXTURE_HEIGHT;
                pos = getBoxPos(groundBox, posX, posY);

                movements = Movement.RIGHT;

                if(pos[0] != -1){
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if(groundBox[index].getImage() != null)
                        result = false;
                }
                break;
            case LEFT:
                posX = getTranslateX() + TEXTURE_WIDTH/2 - BREAKING_DELTA;
                posY = getTranslateY() + TEXTURE_HEIGHT;
                pos = getBoxPos(groundBox, posX, posY);

                movements = Movement.LEFT;

                if(pos[0] != -1){
                    index = getIndexOf(groundBox, pos[0], pos[1]);
                    if(groundBox[index].getImage() != null)
                        result = false;
                }
                break;
        }
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
}
