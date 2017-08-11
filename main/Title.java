package main;

import static helpers.Handler.*;

import helpers.*;
import helpers.StateManager.*;
import ui.*;

public class Title {
    public Texture background = new Texture("null");
    private Button play = new Button("play", 0, 0, 4);
    private Button editor = new Button("editor", 0, 0, 4);
    private Button exit = new Button("exit", 0, 0, 4);
    private int offset = 0;

    private UI ui = new UI();

    public Title() {
        this.createUI();
    }

    /**
     * Sets up the UI
     */
    private void createUI() {
        this.ui.addPanel("panel", WIDTH / 2 - (128 - 8),
                HEIGHT / 2 - (16 + 128 + 64), 1, 3, 64 * 4, 28 * 4);
        this.ui.getPanel("panel").addButton(this.play, 0, 0);
        this.ui.getPanel("panel").addButton(this.editor, 0, 1);
        this.ui.getPanel("panel").addButton(this.exit, 0, 2);
    }

    /**
     * Updates the moving background and UI, draws UI
     */
    public void update() {
        this.updateInput();
        this.updateBackground();
    }

    /**
     * Updates All Possible Input (UI, Buttons, Mouse), Processes Actions
     */
    private void updateInput() {
        if (this.play.isClicked()) {
            StateManager.changeState(GameState.LOBBY);
        } else if (this.editor.isClicked()) {
            StateManager.changeState(GameState.EDITOR);
        } else if (this.exit.isClicked()) {
            endSession();
        }
    }

    /**
     * Updates and Draws the Background
     */
    private void updateBackground() {
        this.offset += .08 * (Boot.dt);
        if (this.offset > 2200) {
            this.offset = 0;
        }
    }

    public void draw() {
        drawQuadTex(this.background, 0, 0, WIDTH, HEIGHT);
        StateManager.map.drawBottom((int) (-12 * PIXELS + this.offset / 1.5),
                (int) (PIXELS * 20 + this.offset / 1.5));
        StateManager.map.drawTop((int) (-12 * PIXELS + this.offset / 1.5),
                (int) (PIXELS * 20 + this.offset / 1.5));
        this.ui.draw();
    }
}
