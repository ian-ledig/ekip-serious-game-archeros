package fr.lekip.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.utils.Item;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PageSummary extends GameGroup {

    private int score;
    private List<Item> items = new ArrayList<>();
    private int indexFirstList;

    private int index = -1;
    private Text txt;
    private double percentEnergy;
    private Item specialItem;

    private GameImage arrowNext;
    private GameImage arrowBefore;
    private GameImage finishBtn;
    private Text txtFinish;
    private GameImage realPicture;

    private StackPane btnPane;
    private StackPane pane;

    public PageSummary(List<Item> found, List<Item> lost, Item pSpecialItem, double percent) {
        try {
            pane = new StackPane();
            // We create the Background Image
            GameImage image = new GameImage(
                    new Image(new FileInputStream("src/assets/textures/pages/summary/end_screen.png")), 1450 / 4,
                    750 / 4, 1150, 590, true);
            pane.getChildren().add(image);
            pane.setPrefSize(1450, 750);
            pane.setAlignment(Pos.CENTER);
            add(pane);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        txt = new Text();
        add(txt);

        // We init the data
        specialItem = pSpecialItem;

        // We put concatenate the 2 list in one and save index where the first list ends
        items.addAll(found);
        items.addAll(lost);
        indexFirstList = found.size() - 1;
        percentEnergy = percent;

        realPicture = new GameImage(null, 275, 200, 200, 200, true);
        add(realPicture);
    }

    public void start() {
        // We calculate the score
        score = calculateScore();

        // Show a text
        try {
            Text end = new Text("FIN DE LA PARTIE");
            end.setFont(Font.loadFont(new FileInputStream(new File("src/assets/font/coco_gothic/CocoGothic_trial.ttf")),
                    25.0));
            end.setX(620);
            end.setY(180);
            add(end);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Then we load image data and the pages
        loadPicture();
        loadPage();

    }

    private int calculateScore() {
        // If the item win was found, we gave him 100 points and then for each other
        // items found we give him 75 points.
        int temp = 0;
        if (items.subList(0, indexFirstList).contains(specialItem)) {
            temp += 100;
            temp += 75 * (items.subList(0, indexFirstList).size() - 1);
        } else {
            temp += 75 * items.subList(0, indexFirstList).size();
        }

        // We add the percentage point
        temp += percentEnergy * 100;

        // TODO add specialist point
        return temp;
    }

    public void loadPage() {
        if (index == -1) {
            try {
                realPicture.setImage(null);

                // TODO le 425 est faux psk ca depends de la pos des items
                // TODO ajouter une appréciation pour combler le vide ? genre si score +de 50%
                // on écrit " PAS MAL" si - "Peut mieux faire !" etc.. ou des étoiles
                String scoreShow = "Points obtenus : " + score + " / 425";
                txt.setText(scoreShow);
                txt.setFont(Font.loadFont(
                        new FileInputStream(new File("src/assets/font/coco_gothic/CocoGothic_trial.ttf")), 18.0));

                txt.setX(500);
                txt.setY(250);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        } else {
            try {
                realPicture.setImage(new Image(new FileInputStream(items.get(index).getTexturePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (index <= indexFirstList) {
                // If the item is in the list of found item we show his description
                txt.setText("Item trouvé ! \n" + items.get(index).getLore());
            } else {
                // If the item is in the list of lost item we show his description
                txt.setText("Item perdu ! \n" + items.get(index).getLore());
            }

        }

    }

    public void loadPicture() {

        if (!(getChildren().contains(arrowNext))) {
            try {

                // We init the arrows and the last button
                arrowNext = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")),
                        980, 550, 150, 30, true);

                arrowBefore = new GameImage(null, 350, 550, 150, 30, true);
                arrowBefore.setRotate(180);

                finishBtn = new GameImage(null, 980, 550, 150, 30, true);
                txtFinish = new Text();
                btnPane = new StackPane();

                btnPane.setTranslateX(980);
                btnPane.setTranslateY(550);

                btnPane.getChildren().add(finishBtn);
                btnPane.getChildren().add(txtFinish);
                btnPane.setAlignment(Pos.CENTER);

                add(btnPane);
                add(arrowNext);
                add(arrowBefore);

                // We add event handler on mouse clicked to recall the loadPage(to change
                // item/page) and loadPicture(to change arrow display)
                arrowNext.setOnMouseClicked((e) -> {
                    index++;
                    loadPage();
                    loadPicture();
                });

                arrowBefore.setOnMouseClicked((e) -> {
                    index--;
                    loadPage();
                    loadPicture();
                });

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }

        if (index >= 0 && index < 3) {
            try {
                // We show both arrows
                arrowBefore.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
                arrowNext.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
                finishBtn.setImage(null);
                txtFinish.setText("");

                btnPane.setOnMouseClicked(null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (index < 0) {
            // We hide the left arrow
            arrowBefore.setImage(null);
        } else if (index == 3) {
            try {
                // We hide the right arrow and display the button to come back to map
                arrowNext.setImage(null);
                finishBtn.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/btn.png")));
                txtFinish.setText("Finir la fouille");

                btnPane.setOnMouseClicked((e) -> {
                    try {
                        Main.setShowedPage(new PageMap(false));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

}
