package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;

public class Map {

    ImageView imageView;
    Group root;

    public Map() throws FileNotFoundException {

        root = new Group();
        loadImage();
        loadPin();

    }

    public void loadImage() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/assets/images/worldMap.png"));
        imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);

        imageView.setFitWidth(1400);
        imageView.setFitHeight(958);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);

    }

    public void loadPin() throws FileNotFoundException {
        Image pin = new Image(new FileInputStream("src/assets/images/pin.png"));
        ImageView image = new ImageView(pin);
        image.setX(150);
        image.setY(150);

        image.setFitWidth(80);
        image.setFitHeight(80);
        image.setPreserveRatio(true);
        root.getChildren().addAll(image);
        image.setOnMouseClicked((e) -> {
            System.out.println("Pin cliqué");
        });
    }

    public Group getGroup() {
        return root;
    }

    public void move(double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
    }

}
