package fr.lekip.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import fr.lekip.utils.Item;
import fr.lekip.utils.Sound;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Show the summary of a mining session
 */
public class PageSummary extends GameGroup {

    private int score;
    private List<Item> items = new ArrayList<>();
    private final int indexFirstList;

    private int index = -1;
    private final Label txt = new Label();
    private final double percentEnergy;
    private final Item specialItem;

    // game images of buttons
    private GameImage arrowNext;
    private GameImage arrowBefore;
    private GameImage finishBtn;
    private final GameImage realPicture;

    private Text txtFinish;
    private int specialistePoints;

    private boolean end = false;

    private StackPane btnPane;
    private StackPane pane;

    private List<GameImage> listStars = new ArrayList<>();

    public PageSummary(List<Item> found, List<Item> lost, Item pSpecialItem, double percent, int pInitScore,
            int maxScore) {
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

        // show the end score of the session
        score = pInitScore;
        specialistePoints = pInitScore;
        txt.setWrapText(true);
        txt.setMaxWidth(650);
        add(txt);

        // We init the data
        specialItem = pSpecialItem;

        // We put concatenate the 2 list in one and save index where the first list ends
        if (found.isEmpty()) {
            items.add(null);
            indexFirstList = 0;
        } else {
            items.addAll(found);
            indexFirstList = found.size() - 1;
        }

        items.addAll(lost);
        percentEnergy = percent;

        realPicture = new GameImage(null, 275, 200, 200, 200, true);
        add(realPicture);
    }

    /**
     * start the summary content
     */
    public void start() {
        // We calculate the score
        score = calculateScore();

        // Show a text
        try {
            Text end = new Text("FIN DE LA PARTIE");
            end.setFont(Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")),
                    18.0));
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

    /**
     * Calcul the score of the session
     * 
     * @return
     */
    private int calculateScore() {
        // If the item win was found, we gave him 100 points and then for each other
        // items found we give him 75 points.
        int temp = score;
        try {
            if (items.subList(0, indexFirstList + 1).contains(specialItem)) {
                temp += 100;
                temp += 75 * (items.subList(0, indexFirstList).size());
            } else {
                temp += 75 * (items.subList(0, indexFirstList).size() + 1);
            }
        } catch (Exception e) {
            temp += 75 * items.subList(0, indexFirstList).size();
        }

        // We add the percentage point
        temp += percentEnergy * 100;

        return temp;
    }

    /**
     * load the page and show content
     */
    public void loadPage() {
        if (index == -1) {
            try {
                realPicture.setImage(null);

                String debrief;
                String signe = "";
                if (specialistePoints > 0) {
                    signe = "+";
                }

                // If indexFirstList is null, that means that there is no found items
                int temp = 1;
                if (items.get(indexFirstList) == null) {
                    temp = 0;
                }

                // We show a detail of all the point gain
                if (items.subList(0, indexFirstList + 1).contains(specialItem)) {
                    debrief = indexFirstList + " objets trouvés : +" + ((indexFirstList) * 75)
                            + "\nObjet objectif trouvé : +100\nEnergie restante : +" + (int) (percentEnergy * 100)
                            + "\nChoix des spécialistes : " + signe + specialistePoints;
                } else {
                    debrief = (temp + indexFirstList) + " objets trouvés : +" + ((indexFirstList + temp) * 75)
                            + "\nEnergie restante : +" + (int) (percentEnergy * 100) + "\nChoix des spécialistes : "
                            + signe + specialistePoints;
                }

                String scoreShow = "Points obtenus : " + score + "\n" + debrief;
                txt.setText(scoreShow);
                txt.setFont(Font.loadFont(
                        new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));
                int nbStars = 0;

                if (score >= 300) {
                    nbStars = 5;
                } else if (score >= 230) {
                    nbStars = 4;
                } else if (score >= 170) {
                    nbStars = 3;
                } else if (score >= 100) {
                    nbStars = 2;
                } else if (score >= 1) {
                    nbStars = 1;
                } else {
                    nbStars = 0;
                }

                int j = 0;
                for (int i = 0; i < 5; i++) {

                    if (nbStars > i) {
                        GameImage star = new GameImage(
                                new Image(new FileInputStream("src/assets/textures/pages/summary/star.png")), 300 + j,
                                350, 150, 150, true);
                        add(star);
                        listStars.add(star);
                    } else {
                        GameImage starGray = new GameImage(
                                new Image(new FileInputStream("src/assets/textures/pages/summary/starGray.png")),
                                300 + j, 350, 150, 150, true);
                        add(starGray);
                        listStars.add(starGray);
                    }
                    j += 160;

                }

                txt.setLayoutX(500);
                txt.setLayoutY(250);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        } else {
            try {
                realPicture.setImage(new Image(new FileInputStream(items.get(index).getTexturePath())));
            } catch (Exception e) {
                // If we dont have found any item
                realPicture.setImage(null);
            }
            if (index <= indexFirstList) {
                end = false;
                if (items.get(index) == null) {
                    txt.setText("Aucun objet trouvé :/ \nPas grave ! Vous ferez mieux la prochaine fois !");
                    try {
                        // If there is no next item, we end
                        if (items.get(index + 1) == null) {
                            end = true;
                        }
                    } catch (Exception e) {
                        end = true;
                    }

                } else {
                    // If the item is in the list of found item we show his description
                    txt.setText("Objet trouvé ! \n" + items.get(index).getLore());

                    // if this is the last item, we end
                    if (index == items.size() - 1) {
                        end = true;
                    }
                }

            } else {
                try {
                    end = false;
                    if (items.get(index) == specialItem) {
                        txt.setText("Vous avez détruis l'objectif !\n" + items.get(index).getLore());
                        end = true;

                    } else {
                        // If the item is in the list of lost item we show his description
                        txt.setText("Objet perdu ! \n" + items.get(index).getLore());

                        // if this is the last item, we end
                        if (index == items.size() - 1) {
                            end = true;
                        }
                    }
                } catch (Exception e) {
                    end = true;

                }

            }

        }

    }

    /**
     * load the pictures of items found or to found
     */
    public void loadPicture() {
        if (!(getChildren().contains(arrowNext))) {
            try {

                // We init the arrows and the last button
                arrowNext = new GameImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")),
                        980, 550, 150, 30, true);

                arrowBefore = new GameImage(null, 350, 550, 150, 30, true);

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
                    Sound.BUTTON_END.getMediaPlayer().play();
                    index++;

                    loadPage();
                    loadPicture();
                });

                arrowBefore.setOnMouseClicked((e) -> {
                    Sound.BUTTON_END.getMediaPlayer().play();
                    index--;
                    end = false;
                    loadPage();
                    loadPicture();
                });

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }

        if (end) {
            try {

                // Set stars to null
                for (GameImage item : listStars) {
                    item.setImage(null);
                }

                arrowBefore
                        .setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow_back.png")));
                // We hide the right arrow and display the button to come back to map
                arrowNext.setImage(null);
                finishBtn.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/btn.png")));
                txtFinish.setText("Finir la fouille");

                btnPane.setOnMouseClicked((e) -> {
                    Sound.QUIT.getMediaPlayer().play();
                    try {
                        Main.setShowedPage(new PageMap(false));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (index < 0) {
            // We hide the left arrow
            arrowBefore.setImage(null);
            try {
                arrowNext.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            finishBtn.setImage(null);
            txtFinish.setText("");
            btnPane.setOnMouseClicked(null);
        } else {
            try {

                for (GameImage item : listStars) {
                    item.setImage(null);
                }

                // We show both arrows
                arrowBefore
                        .setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow_back.png")));

                arrowNext.setImage(new Image(new FileInputStream("src/assets/textures/pages/summary/arrow.png")));
                finishBtn.setImage(null);
                txtFinish.setText("");

                btnPane.setOnMouseClicked(null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}
