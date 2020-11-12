package fr.lekip.pages;

public enum SkyboxType {
    BLUE_SKY(0), BLUE_SKY_CLOUDS(1), DARK_SKY(2);

    private int id;

    SkyboxType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
