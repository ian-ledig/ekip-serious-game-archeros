package fr.lekip.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameSpecialist extends GameGroup {

    private Pane specialist;
    private int x;
    private int y;
    private int id;
    private boolean inside = false;
    private Pane information;

    private Color[] colors = { Color.AQUAMARINE, Color.BLUEVIOLET, Color.LAVENDER };
    private String[] info = { "Le Xylologue étudie les objets en bois, leur nature et leur fonction.",
            "L'Anthropologue étudie les ossements humains ainsi que leur méthode d’inhumation.",
            "Le Céramologue étudie les éléments de vaisselle en terre cuite, leur forme, leur fonction et leur mode de façonnage." };
    private String[] names = { "Xylologue", "Anthropologue", "Céramologue" };

    public GameSpecialist(int pX, int pY, int pId) {
        Random rand = new Random();
        specialist = new Pane();
        specialist.setTranslateX(pX);
        specialist.setTranslateY(pY);

        x = pX;
        y = pY;
        id = pId;

        Rectangle backgroundRec = new Rectangle(150, 180);
        backgroundRec.setY(55);
        backgroundRec.setFill(colors[rand.nextInt(3)]);

        GameImage help;
        try {

            Pane infoPop = new Pane();
            infoPop.setPrefSize(50, 50);
            infoPop.setTranslateX(50);
            help = new GameImage(new Image(new FileInputStream("src/assets/textures/specialist/info.png")), 0, -5, 50,
                    50, true);
            infoPop.setOnMouseEntered((e) -> {
                if (inside) {

                } else {
                    int i = -50;
                    information = new Pane();
                    information.setTranslateX(0);
                    information.setTranslateY(-80);

                    Label description = new Label(info[id]);
                    description.setWrapText(true);
                    description.setMaxWidth(240);
                    description.setTranslateY(10);
                    description.setBackground(
                            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                    information.getChildren().add(description);
                    specialist.getChildren().add(information);
                    inside = true;
                }
            });

            infoPop.setOnMouseExited((e) -> {
                inside = false;
                specialist.getChildren().remove(information);
            });
            infoPop.getChildren().add(help);
            specialist.getChildren().add(infoPop);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CheckBox checkBox1 = new CheckBox("Recruter");
        checkBox1.setTranslateX(50);
        checkBox1.setTranslateY(240);
        specialist.getChildren().addAll(backgroundRec, checkBox1);

        loadPicture(id);
    }

    public Pane getSpecialist() {
        return specialist;
    }

    public void loadPicture(int id) {
        try {
            GameImage head = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/specialist/" + names[id] + ".png")), 0, 55, 150,
                    150, true);

            StackPane name = new StackPane();
            name.setPrefSize(150, 10);
            Text nameId = new Text(names[id]);
            nameId.setFont(Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")),
                    18.0));

            name.setTranslateX(0);
            name.setTranslateY(205);
            name.getChildren().add(nameId);
            name.setAlignment(Pos.CENTER);
            specialist.getChildren().addAll(head, name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
