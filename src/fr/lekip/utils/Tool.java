package fr.lekip.utils;

public enum Tool {

    SHOVEL("src/assets/textures/tools/shovel.png", 2),
    PICKAXE("src/assets/textures/tools/pickaxe.png", 3);

    private String texturePath;
    private int strength;

    Tool(String texturePath, int strength){
        this.texturePath = texturePath;
        this.strength = strength;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getStrength() {
        return strength;
    }
}
