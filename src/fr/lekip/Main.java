package fr.lekip;

import fr.lekip.components.GameGroup;
import fr.lekip.pages.PageMap;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static GameGroup showedPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup scene
        Group root = new Group();
        Scene scene = new Scene(root, 850, 500);
        scene.setFill(Color.TRANSPARENT);

        // Page creation
        setShowedPage(new PageMap());
        root.getChildren().add(showedPage);

        primaryStage.setTitle("L'Ekip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

<<<<<<< HEAD:src/sample/Main.java
        // Test !

        // test

=======
    public static void setShowedPage(GameGroup showedPage) {
        Main.showedPage = showedPage;
>>>>>>> master:src/fr/lekip/Main.java
    }

    public static void main(String[] args) {
        launch(args);
    }
}
