package fr.lekip.utils;

/**
 * Enum of movements which is used in page mining
 */
public enum Movement {

    UP("src/assets/textures/player/player_up.png"),
    DOWN("src/assets/textures/player/player_down.png"),
    RIGHT("src/assets/textures/player/player_right.png"),
    LEFT("src/assets/textures/player/player_left.png");

    private final String texturePath;

    Movement(String texturePath){
        this.texturePath = texturePath;
    }

    /**
     * @return texture path of the movement type
     */
    public String getTexturePath() {
        return texturePath;
    }
}
