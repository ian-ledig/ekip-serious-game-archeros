package fr.lekip.utils;

public enum SkyboxType {
    PLAIN(0), DESERT(1), MOUNTAIN(2);

    private int id;

    SkyboxType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
