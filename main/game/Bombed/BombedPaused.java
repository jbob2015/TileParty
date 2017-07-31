package main.game.Bombed;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import ui.*;

public class BombedPaused {

    private static UI ui;

    public BombedPaused() {
        createUI();
    }

    /**
     * Updates All Aspects of GamePaused, Draws the UI
     */
    public void update() {
        updateInput();
        ui.draw();
    }

    /**
     * Creates UI for GamePaused
     */
    private static void createUI() {
        ui = new UI();
        ui.addPanel("panel", WIDTH / 2 - 128 - 12, HEIGHT / 5, 1, 3, 256, 128);
        ui.getPanel("panel").addButton("resume", 0, 0);
    }

    /**
     * Updates All Possible Input (UI, Buttons, Mouse), Processes Actions
     */
    private static void updateInput() {
        if (ui.isClicked("resume")) {
            StateManager.changeState(GameState.BOMBED);
        } else if (Keyboard.isClicked(Key.ESCAPE)
                || Keyboard.isClicked(Key.E)) {
            StateManager.changeState(GameState.BOMBED);
        }
    }
}
