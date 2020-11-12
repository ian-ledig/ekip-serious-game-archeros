package fr.lekip.inputs;

import fr.lekip.components.GameGroup;
import javafx.event.EventHandler;

public abstract class GameEventHandler implements EventHandler {

    public GameEventHandler(GameGroup group){
        group.setFocusTraversable(true);
        loadEventHandler(group);
    }

    /**
     * Load the event structure
     * @param group to apply the event handler
     */
    public abstract void loadEventHandler(GameGroup group);
}
