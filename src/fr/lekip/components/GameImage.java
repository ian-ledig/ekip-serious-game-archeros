package fr.lekip.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameImage extends ImageView {



    public GameImage(Image img, double x, double y, double width, double height, boolean preserveRatio){
        setImage(img);
        xPos = x;
        yPos = y;
        setX(x);
        setY(y);
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(preserveRatio);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new GameImage(getImage(), getX(), getY(), getFitWidth(), getFitHeight(), isPreserveRatio());
    }
}
