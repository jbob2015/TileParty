package main.editor;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import ui.*;

public class EditorPaused {

    private static UI ui;

    public EditorPaused() {
        createUI();
    }

    /**
     * Updates All Aspects of EditorPaused, Draws the UI
     */
    public void update() {
        updateInput();
        ui.draw();
    }

    /**
     * Creates UI for EditorPaused
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
            StateManager.changeState(GameState.EDITOR);
        } else if (ui.isClicked("exit")) {
            StateManager.changeState(GameState.TITLE);
        } else if (Keyboard.isClicked(Key.ESCAPE)) {
            StateManager.changeState(GameState.EDITOR);
        }
    }

    public void draw() {
        ui.draw();
    }

}
