package main.game;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import ui.*;

public class GamePaused {

    private static UI ui;

    public GamePaused() {
        createUI();
    }

    /**
     * Updates All Aspects of GamePaused, Draws the UI
     */
    public void update() {
        updateInput();
    }

    /**
     * Creates UI for GamePaused
     */
    private static void createUI() {
        ui = new UI();
        ui.addPanel("panel", WIDTH / 2 - (128 - 8),
                HEIGHT / 2 - (16 + 128 + 64), 1, 3, 64 * 4, 28 * 4);
        ui.getPanel("panel").addButton("resume", 0, 0, 4);
        ui.getPanel("panel").addButton("exit", 0, 2, 4);
    }

    /**
     * Updates All Possible Input (UI, Buttons, Mouse), Processes Actions
     */
    private static void updateInput() {
        if (ui.isClicked("resume")) {
            StateManager.changeState(GameState.GAME);
        } else if (ui.isClicked("menu")) {
            StateManager.changeState(GameState.TITLE);
        } else if (Keyboard.isClicked(Key.ESCAPE)
                || Keyboard.isClicked(Key.E)) {
            StateManager.changeState(GameState.GAME);
        }
    }

    public void draw() {
        ui.draw();
    }
}
