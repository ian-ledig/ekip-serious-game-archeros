package fr.lekip.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameImage extends ImageView {

    double xPos;
    double yPos;

    public GameImage(Image img, double x, double y, double width, double height, boolean preserveRatio) {
        setImage(img);
        xPos = x;
        yPos = y;
        setX(x);
        setY(y);
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(preserveRatio);
    }

    public double getXImage() {
        return xPos;
    }

    public double getYImage() {
        return yPos;
    }
}
