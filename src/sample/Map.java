package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Map {

    ImageView imageView;
    Group root;

    public Map() throws FileNotFoundException {

        root = new Group();
        loadImage();
        loadText();

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

    public void loadText() {
        // Create text
        Text text = new Text("Cliquez pour commencer...");
        text.setTranslateX(250);
        text.setTranslateY(250);
        text.setStyle("-fx-fill: purple;-fx-font: 30 arial; ");

        root.getChildren().add(text);

        // Create animation
        final ScaleTransition stBig = new ScaleTransition();
        stBig.setNode(text);
        stBig.setFromX(1.0);
        stBig.setFromY(1.0);

        // Final size
        stBig.setToX(1.3);
        stBig.setToY(1.3);
        stBig.setDuration(new Duration(1500));
        stBig.setAutoReverse(true);
        stBig.setCycleCount(-1);
        stBig.play();

        // Add event for starting
        root.setOnMouseClicked((e) -> {
            stBig.stop();
            root.getChildren().remove(text);
            try {
                loadPin();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            // ici on load le menu aussi <---

            // Delete event
            root.setOnMouseClicked(null);
        });
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
