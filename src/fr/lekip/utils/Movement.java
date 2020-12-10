package fr.lekip.utils;

public enum Movement {

    UP("src/assets/textures/player/player_up.png"),
    DOWN("src/assets/textures/player/player_down.png"),
    RIGHT("src/assets/textures/player/player_right.png"),
    LEFT("src/assets/textures/player/player_left.png");

    private final String texturePath;

    Movement(String texturePath){
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
