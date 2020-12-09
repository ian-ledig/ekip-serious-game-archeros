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

public class PageIntro extends GameGroup {

    private Text txt;
    private int index;
    private String[] dialog = {
            "Bienvenue sur [NOM] ! \nVous allez devoir découvrir des vestiges du passé en faisant attention de ne pas endommager les trouvailles !",
            "Vous allez avoir une carte du monde où se situe des repères de site archéologique à fouiller !",
            "Vous pouvez soit cliquer sur les repères sur la carte ou alors vous pouvez cliquer sur le menu qui va se trouver à droite \npour être directement transporter sur le site",
            "Vous allez devoir fouiller le sol à l'aide de plusieurs outils en faisant attention de ne pas endommager vos trouvailles !\nAttention ! Toutes les erreurs peuvent vous faire perdre !",
            "Appuyez sur n'importe quelle touche pour commencer..." };

    public PageIntro() throws FileNotFoundException {
        Image overlayDialog = new Image(new FileInputStream("src/assets/textures/pages/main/textbox.png"));

        StackPane mapp = new StackPane();
        GameImage image = new GameImage(overlayDialog, 0, 600, 1450, 150, false);

        txt = new Text(dialog[0]);
        txt.setFont(
                Font.loadFont(new FileInputStream(new File("src/assets/font/squad_goals/SquadGoalsTTF.ttf")), 18.0));

        mapp.getChildren().addAll(image);
        mapp.getChildren().addAll(txt);

        // TODO Change pos of text
        // TODO Use CSS padding or margin to change text's pos
        mapp.setTranslateY(600);
        mapp.setAlignment(Pos.CENTER);
        add(mapp);

        handleEvent();

    }

    public void handleEvent() {
        index = 0;

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
