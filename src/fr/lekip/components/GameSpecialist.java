package fr.lekip.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.lekip.utils.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
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
    private CheckBox checkBox1;
    private boolean correct;

    private Color[] colors = { Color.AQUAMARINE, Color.BLUEVIOLET, Color.LAVENDER };
    private String[] info = { "Le Xylologue étudie les objets en bois, leur nature et leur fonction.",
            "L'Anthropologue étudie les ossements humains ainsi que leur méthode d’inhumation.",
            "Le Céramologue étudie les éléments de vaisselle en terre cuite, leur forme, leur fonction et leur mode de façonnage.",
            "Le Numismate est spécialiste des monnaies anciennes, il détermine leur provenance, leur technique et date de fabrication.",
            "L'Archéogéologue est spécialiste des roches et de la recherche des carrières exploitées dans l’antiquité.",
            "Le Paléométallurgiste est spécialiste des objets métalliques et de leur technique de fabrication." };
    private String[] names = { "Xylologue", "Anthropologue", "Céramologue", "Numismate", "Archéogéologue",
            "Paléométallurgiste" };
    private static Item[][] specialistItems = { {}, { Item.PRIEST }, { Item.STATUETTE, Item.STATUETTE_G },
            { Item.COIN, Item.BUTTON, Item.ARTEFACT }, { Item.MICROLITH_SCRAPER, Item.YORKSHIRE_SCRAPER, Item.STATUE },
            { Item.NAIL, Item.COMB, Item.STATUETTE_BRONZE } };

    public GameSpecialist(int pX, int pY, int pId, boolean pCorrect) {
        Random rand = new Random();
        specialist = new Pane();

        x = pX;
        y = pY;
        id = pId;
        correct = pCorrect;

        specialist.setTranslateX(x);
        specialist.setTranslateY(y);

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

        int perc;
        if (correct) {
            perc = (int) (Math.random() * (25 - 10) + 10);
        } else {
            perc = (int) (Math.random() * (15 - 0) + 0);
        }
        Text percentage = new Text(String.valueOf(perc) + "%");
        percentage.setX(50);
        percentage.setY(250);
        checkBox1 = new CheckBox("Recruter");
        checkBox1.setTranslateX(50);
        checkBox1.setTranslateY(260);
        specialist.getChildren().addAll(backgroundRec, percentage, checkBox1);

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

    public List<Item> getItems() {
        // pa sur
        return Arrays.asList(specialistItems[id]);
    }

    public void setPos(int x, int y) {
        specialist.setTranslateX(x);
        specialist.setTranslateY(y);
    }

    public static GameSpecialist getSpecificSpecialist(Item item) {
        for (int i = 0; i < 6; i++) {
            if (Arrays.asList(specialistItems[i]).contains(item)) {
                return new GameSpecialist(0, 0, i, true);
            }
        }
        return null;
    }

    public boolean getChecked() {
        return checkBox1.isSelected();
    }

    public boolean getCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return names[id];
    }
}
