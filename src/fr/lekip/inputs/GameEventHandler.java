package fr.lekip.inputs;

import fr.lekip.components.GameGroup;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class GameEventHandler implements EventHandler<MouseEvent> {

    public GameEventHandler(GameGroup group) {}

    @Override
    public void handle(MouseEvent mouseEvent) {}
}
