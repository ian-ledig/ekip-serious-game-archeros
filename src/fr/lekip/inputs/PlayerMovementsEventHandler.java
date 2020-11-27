package fr.lekip.inputs;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GamePlayer;
import fr.lekip.pages.PageMining;
import fr.lekip.utils.Direction;
import fr.lekip.utils.Movement;
import fr.lekip.utils.Tool;
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

                int delta = 10;
                switch (keyEvent.getCode()){
                    // Left movement
                    case Q:
                    case LEFT:
                        player.setMovements(Movement.LEFT);
                        if(player.getTranslateX() - delta > 0 && player.canPlayerGo(Direction.LEFT))
                            player.decrementX(delta);
                        break;

                    // Right movement
                    case D:
                    case RIGHT:
                        player.setMovements(Movement.RIGHT);
                        if(player.getTranslateX() - delta < Main.WINDOWS_WIDTH - 80 && player.canPlayerGo(Direction.RIGHT))
                            player.incrementX(delta);
                        break;

                    // Down movement
                    case DOWN:
                    case S:
                        player.setMovements(Movement.DOWN);
                        if(player.canPlayerGo(Direction.DOWN))
                            player.decrementY(delta);
                        break;

                    // Up movement
                    case UP:
                    case Z:
                        player.setMovements(Movement.UP);
                        if(player.canPlayerGo(Direction.UP))
                            player.incrementY(delta);
                        break;

                    // Switch tool
                    case F:
                        int toolIndex = 0;

                        if(player.getTool() != null){
                            toolIndex = player.getTool().ordinal() + 1;
                            if(toolIndex == Tool.values().length)
                                toolIndex = 0;
                        }
                        else
                            toolIndex = Tool.SHOVEL.ordinal();

                        player.setTool(Tool.values()[toolIndex]);
                        break;
                }
            });

            group.setOnMousePressed(mouseEvent -> {
                switch (mouseEvent.getButton()){
                    // Try to break
                    case PRIMARY:
                        player.tryToBreak();
                        break;
                }
            });
        }
    }

    @Override
    public void handle(Event event) {}
}
