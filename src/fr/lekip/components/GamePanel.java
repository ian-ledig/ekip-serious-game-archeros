package fr.lekip.components;

import javafx.scene.Group;
import javafx.scene.Node;

public class GamePanel extends Group {

    public void add(Node node){
        getChildren().add(node);
    }

    public void addAll(Node node){
        getChildren().addAll(node);
    }

    public void remove(Node node){
        getChildren().remove(node);
    }
}
