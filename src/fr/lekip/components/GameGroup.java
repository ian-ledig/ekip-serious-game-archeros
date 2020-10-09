package fr.lekip.components;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;

import java.lang.reflect.InvocationTargetException;

public class GameGroup extends Group {

    public void add(Node node){
        getChildren().add(node);
    }

    public void addAll(Node node){
        getChildren().addAll(node);
    }

    public void remove(Node node){
        getChildren().remove(node);
    }

    public void addEventHandler(Class<? extends EventHandler> eventHandler){
        try {
            eventHandler.getConstructor(GameGroup.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
