package fr.lekip.inputs;

import fr.lekip.Main;
import fr.lekip.components.GameGroup;
import fr.lekip.components.GamePlayer;
import fr.lekip.pages.PageMining;
import fr.lekip.utils.Direction;
import fr.lekip.utils.Movement;
import fr.lekip.utils.Tool;
import javafx.event.Event;
import javafx.scene.input.KeyCode;

public class PlayerMovementsEventHandler extends GameEventHandler {

    public PlayerMovementsEventHandler(GameGroup group) {
        super(group);
    }

    @Override
    public void loadEventHandler(GameGroup group) {
        if (group instanceof PageMining) {
            PageMining page = (PageMining) group;
            GamePlayer player = page.getPlayer();

            if (player != null && player.getParent() instanceof PageMining) {


                group.setOnKeyPressed(keyEvent -> {

                    if(keyEvent.getCode().equals(KeyCode.ESCAPE) && !page.isIntro()){
                        page.switchPause();
                        return;
                    }

                    if(!page.isInPause() && !page.isIntro()) {
                        int delta = 10;
                        switch (keyEvent.getCode()) {
                            // Left movement
                            case Q:
                            case LEFT:
                                player.setMovements(Movement.LEFT);
                                if (player.getTranslateX() - delta > 0 && player.canPlayerGo(Direction.LEFT))
                                    player.decrementX(delta);
                                break;

                            // Right movement
                            case D:
                            case RIGHT:
                                player.setMovements(Movement.RIGHT);
                                if (player.getTranslateX() - delta < Main.WINDOWS_WIDTH - 80
                                        && player.canPlayerGo(Direction.RIGHT))
                                    player.incrementX(delta);
                                break;

                            // Down movement
                            case DOWN:
                            case S:
                                player.setMovements(Movement.DOWN);
                                if (player.canPlayerGo(Direction.DOWN))
                                    player.decrementY(delta);
                                break;

                            // Up movement
                            case UP:
                            case Z:
                                player.setMovements(Movement.UP);
                                if (player.canPlayerGo(Direction.UP))
                                    player.incrementY(delta);
                                break;

                            // Switch tool
                            case F:
                                int toolIndex = 0;

                                if (player.getTool() != null) {
                                    toolIndex = player.getTool().ordinal() + 1;
                                    if (toolIndex == Tool.values().length)
                                        toolIndex = 0;
                                } else
                                    toolIndex = Tool.SHOVEL.ordinal();

                                player.setTool(Tool.values()[toolIndex]);
                                break;
                        }
                    }
                });


                group.setOnMousePressed(mouseEvent -> {
                    if(!page.isInPause() && !page.isIntro()) {
                        // If the tool is the probe, we call the function probe()
                        if (player.getTool() == Tool.PROBE) {
                            player.probe();
                        } else {
                            switch (mouseEvent.getButton()) {
                                // Try to break
                                case PRIMARY:
                                    player.tryToBreak();
                                    break;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void handle(Event event) {
    }
}
