package fr.lekip.components;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;

import java.lang.reflect.InvocationTargetException;

public class GameGroup extends Group {

    /**
     * Add a component to the current Group
     * @param node component to add
     */
    public void add(Node node){
        getChildren().add(node);
    }

    /**
     * Remove a component to the current Group
     * @param node component to remove
     */
    public void remove(Node node){
        getChildren().remove(node);
    }

    /**
     * Add an event handler to the current Group
     * @param eventHandler the event handler class
     */
    public void addEventHandler(Class<? extends EventHandler> eventHandler){
        try {
            eventHandler.getConstructor(GameGroup.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
