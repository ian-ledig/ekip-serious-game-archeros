package fr.lekip.utils;

public enum Tool {

    PICKAXE("src/assets/textures/tools/pickaxe.png", 2),
    SHOVEL("src/assets/textures/tools/shovel.png", 1),
    DYNAMITER("src/assets/textures/tools/dynamiter.png", 4),
    PROBE("src/assets/textures/tools/probe.png", 7);

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
