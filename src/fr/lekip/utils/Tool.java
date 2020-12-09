package fr.lekip.utils;

public enum Tool {

    SHOVEL("src/assets/textures/tools/shovel.png", 1),
    PICKAXE("src/assets/textures/tools/pickaxe.png", 2),
    PROBE("src/assets/textures/tools/probe.png", 7),
    DYNAMITER("src/assets/textures/tools/dynamiter.png", 4);

    private String texturePath;
    private int strength;

    Tool(String texturePath, int strength) {
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
