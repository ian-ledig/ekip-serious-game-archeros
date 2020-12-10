package fr.lekip.utils;

/**
 * Enum of tools which can be used in page mining
 */
public enum Tool {

    SHOVEL("src/assets/textures/tools/shovel.png", 1),
    PICKAXE("src/assets/textures/tools/pickaxe.png", 2),
    PROBE("src/assets/textures/tools/probe.png", 7),
    DYNAMITER("src/assets/textures/tools/dynamiter.png", 4);

    private final String texturePath;
    private final int strength;

    Tool(String texturePath, int strength) {
        this.texturePath = texturePath;
        this.strength = strength;
    }

    /**
     * @return texture path of the tool
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * @return strength of the tool
     */
    public int getStrength() {
        return strength;
    }
}
