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

    private StackPane pane;

    public PageSummary(List<Item> found, List<Item> lost, Item specialItem, double percent) {
        try {
            pane = new StackPane();
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
        items.addAll(found);
        items.addAll(lost);
        indexFirstList = found.size() - 1;
        percentEnergy = percent;
    }

    public void start() {
        score = calculateScore();

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
        loadPicture();
        loadText();

    }

    private int calculateScore() {
        int temp = 0;
        if (items.subList(0, indexFirstList).contains(specialItem)) {
            temp += 100;
            temp += 75 * (items.subList(0, indexFirstList).size() - 1);
        } else {
            temp += 75 * items.subList(0, indexFirstList).size();
        }

        temp += percentEnergy * 100;
        return temp;
    }

    public void loadText() {
        if (index == -1) {
            try {
                // TODO le 425 est faux psk ca depends de la pos des items
                String scoreShow = "Points obtenus : " + score + " / 425";
                txt.setText(scoreShow);
                txt.setFont(Font.loadFont(
                        new FileInputStream(new File("src/assets/font/coco_gothic/CocoGothic_trial.ttf")), 18.0));

                txt.setX(350);
                txt.setY(250);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        } else {
            if (index <= indexFirstList) {
                txt.setText("Item trouvé ! \n" + items.get(index).getLore());
            } else {
                txt.setText("Item perdu ! \n" + items.get(index).getLore());
            }

        }

    }

    public void loadPicture() {

        if (!(getChildren().contains(arrowNext))) {
            try {
                arrowNext = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")),
                        980, 550, 150, 30, true);

                arrowBefore = new GameImage(null, 350, 550, 150, 30, true);
                arrowBefore.setRotate(180);

                finishBtn = new GameImage(null, 980, 550, 150, 30, true);
                txtFinish = new Text();
                StackPane btnPane = new StackPane();

                btnPane.setTranslateX(980);
                btnPane.setTranslateY(550);

                btnPane.getChildren().add(finishBtn);
                btnPane.getChildren().add(txtFinish);
                btnPane.setAlignment(Pos.CENTER);

                add(btnPane);
                add(arrowNext);
                add(arrowBefore);

                arrowNext.setOnMouseClicked((e) -> {
                    System.out.println("cliqué");
                    index++;
                    loadText();
                    loadPicture();
                });

                arrowBefore.setOnMouseClicked((e) -> {
                    index--;
                    loadText();
                    loadPicture();
                });

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

        if (index >= 0 && index < 3) {
            try {
                arrowBefore.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
                arrowNext.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
                finishBtn.setImage(null);
                txtFinish.setText("");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (index < 0) {
            arrowBefore.setImage(null);
        } else if (index == 3) {
            try {
                arrowNext.setImage(null);
                finishBtn.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/btn.png")));
                txtFinish.setText("Finir la fouille");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

}
