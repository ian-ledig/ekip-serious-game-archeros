package fr.lekip.utils;

import fr.lekip.components.GameImage;

public enum Tools {

    SHOVEL("src/assets/textures/tools/shovel.png", 10);

    private GameImage gameImage;
    private int strength;

    Tools(String texturePath, int strength){
        this.strength = strength;
    }

    public GameImage getGameImage() {
        return gameImage;
    }

    public int getStrength() {
        return strength;
    }
}
