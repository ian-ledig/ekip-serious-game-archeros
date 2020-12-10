package fr.lekip.utils;

/**
 * Enum of skybox types which is used in page mining
 */
public enum SkyboxType {
    PLAIN(0), DESERT(1), MOUNTAIN(2);

    private final int id;

    SkyboxType(int id){
        this.id = id;
    }

    /**
     * @return id of the skybox
     */
    public int getId() {
        return id;
    }
}
