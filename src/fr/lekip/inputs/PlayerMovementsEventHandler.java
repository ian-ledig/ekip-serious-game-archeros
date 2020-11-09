package fr.lekip.inputs;

import fr.lekip.components.GameGroup;
import fr.lekip.components.GamePlayer;
import fr.lekip.pages.PageMining;
import javafx.event.Event;

public class PlayerMovementsEventHandler extends GameEventHandler{

    private GamePlayer player;

    public PlayerMovementsEventHandler(GameGroup group) {
        super(group);
    }

    @Override
    public void loadEventHandler(GameGroup group) {
        if(group instanceof PageMining)
            player = ((PageMining) group).getPlayer();

        if(player != null){
            group.setOnKeyPressed(keyEvent -> {

                switch (keyEvent.getCode()){
                    case Q:
                        player.decrementX(10);
                        break;
                    case D:
                        player.incrementX(10);
                        break;
                    case SPACE:
                        break;
                }
            });
        }
    }

    @Override
    public void handle(Event event) {

    }
}
