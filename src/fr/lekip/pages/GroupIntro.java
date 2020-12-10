package fr.lekip.pages;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GameImage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Show the intro in the map page
 */
public class GroupIntro extends GameGroup {

    private final Text txt;
    private int index;
    private final String[] dialog = {
            "Bienvenue sur Archeroes ! \nVous allez devoir découvrir des vestiges du passé en faisant attention de ne pas endommager les trouvailles !",
            "Vous allez avoir une carte du monde où se situe des repères de sites archéologiques à fouiller !",
            "Vous pouvez soit cliquer sur les repères sur la carte, soit cliquer sur le menu qui va se trouver à droite \npour être directement transporté sur le site",
            "Vous allez devoir fouiller le sol à l'aide de plusieurs outils en faisant attention de ne pas endommager vos trouvailles !\nAttention ! Toutes les erreurs peuvent vous faire perdre !",
            "Appuyez sur n'importe quelle touche pour commencer..." };

    public GroupIntro() throws FileNotFoundException {
        Image imgCharacter = new Image(new FileInputStream("src/assets/textures/pages/main/introCharacter.png"));
        Image imgDialog = new Image(new FileInputStream("src/assets/textures/pages/main/textbox.png"));

        StackPane map = new StackPane();
        GameImage gmgCharacter = new GameImage(imgCharacter, 20, 250, 630/2, 1425/2, false);
        GameImage gmgDialog = new GameImage(imgDialog, 0, 600, 1450, 150, false);

        add(gmgCharacter);

        txt = new Text(dialog[0]);
        txt.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));

        map.getChildren().addAll(gmgDialog, txt);

        map.setTranslateY(600);
        map.setAlignment(Pos.CENTER);
        add(map);

        // load event
        handleEvent();
    }

    /**
     * Wait for a player action
     */
    public void handleEvent() {
        index = 0;

        // wait for player mouse click
        setOnMouseClicked((e) -> {
            index++;

            // Change text
            setText(index);
            if (index == dialog.length - 1) {
                // Delete event
                setOnMouseClicked(null);
            }
        });

    }

    /**
     * @param pIndex text to show in
     */
    private void setText(int pIndex) {
        txt.setText(dialog[pIndex]);
    }

    public Boolean isFinished() {
        if (index != dialog.length - 1) {
            return false;
        }
        return true;
    }

}
