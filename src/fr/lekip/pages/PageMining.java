package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.components.GamePlayer;
import fr.lekip.inputs.PlayerMovementsEventHandler;
import fr.lekip.utils.GroundType;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class PageMining extends GameGroup {

    public static final int GROUND_BLOCKS_NUMBER = 2187;
    public static final int GROUND_BLOCKS_LINE_NUMBER = 81;
    private GameImage[] groundBox = new GameImage[GROUND_BLOCKS_NUMBER];
    private GamePlayer player = new GamePlayer(this);

    public PageMining(SkyboxType skyboxType, List<GroundType> groundTypes) throws FileNotFoundException {
        Image skyBox = new Image(new FileInputStream("src/assets/textures/pages/mining/skybox" + skyboxType.getId() + ".png"));

        // Create the map
        GameImage imgSkyBox = new GameImage(skyBox, 0, 0, skyBox.getWidth(), skyBox.getHeight(), true);
        add(imgSkyBox);

        int x = 0;
        int y = 262;
        try {
            for(int i = 0; i < GROUND_BLOCKS_NUMBER; i++){
                if(i < 780) {
                    groundBox[i] = groundTypes.get(0).getGameImage();
                }
                else
                    groundBox[i] = groundTypes.get(1).getGameImage();

                // Drawing ground blocks
                x += 18;
                if(i % GROUND_BLOCKS_LINE_NUMBER == 0){
                    y += 18;
                    x = 0;
                }

                groundBox[i].setX(x);
                groundBox[i].setY(y);
                add(groundBox[i]);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        add(player);

        // Add map event handler
        addEventHandler(PlayerMovementsEventHandler.class);
    }

    public GameImage[] getGroundBox() {
        return groundBox;
    }

    public void setGroundBox(GameImage[] groundBox) {
        this.groundBox = groundBox;
    }

    public GamePlayer getPlayer() {
        return player;
    }
}