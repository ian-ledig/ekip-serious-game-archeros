package fr.lekip.utils;

public enum Movement {

    IDLE("src/assets/textures/player/player_idle.png"),
    RIGHT("src/assets/textures/player/player_right.png"),
    LEFT("src/assets/textures/player/player_left.png");

    private String texturePath;

    Movement(String texturePath){
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
