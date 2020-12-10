package fr.lekip.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameImage extends ImageView {

    public GameImage(Image img, double x, double y, double width, double height, boolean preserveRatio){
        setImage(img);
        setX(x);
        setY(y);
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(preserveRatio);
    }

    /**
     * Clone the current game image
     * @return the new instance of game image cloned
     * @throws CloneNotSupportedException if the clone fails
     */
    @Override
    public Object clone() {
        return new GameImage(getImage(), getX(), getY(), getFitWidth(), getFitHeight(), isPreserveRatio());
    }

    /**
     * @return get the X position
     */
    public double getXImage() {
        return xProperty().get();
    }

    /**
     * @return get the Y position
     */
    public double getYImage() {
        return yProperty().get();
    }
}
