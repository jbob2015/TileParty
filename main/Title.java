package main;

import static helpers.Handler.*;
import static org.lwjgl.opengl.GL11.*;

import helpers.*;
import helpers.StateManager.*;
import ui.*;

public class Title {

    private Texture background = new Texture("city");
    private Button play = new Button("play", 0, 0);
    private int offset = 0, oldOffset = 0;

    private UI ui = new UI();

    public Title() {
        this.createUI();
    }

    /**
     * Sets up the UI
     */
    private void createUI() {
        this.ui.addPanel("panel", WIDTH / 2 - (128 - 8),
                HEIGHT / 2 - (16 + 128 + 64), 1, 3, 256, 128);
        this.ui.getPanel("panel").addButton(this.play, 0, 0);
        this.ui.getPanel("panel").addButton("editor", 0, 1);
        this.ui.getPanel("panel").addButton("exit", 0, 2);
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
        if (this.ui.isClicked("play")) {
            StateManager.changeState(GameState.LOBBY);
        } else if (this.ui.isClicked("editor")) {
            StateManager.changeState(GameState.EDITOR);
        } else if (this.ui.isClicked("exit")) {
            endSession();
        }
    }

    /**
     * Updates and Draws the Background
     */
    private void updateBackground() {
        this.oldOffset = this.offset;
        this.offset += .2 * (Boot.dt);
        if (this.offset > 8192) {
            this.offset = 0;
        }
        if (this.offset < 0) {
            this.offset = 8192;
        }
    }

    public void draw() {
        glTranslatef(
                -(this.offset * Boot.alpha + this.oldOffset * (1 - Boot.alpha)),
                0, 0);
        drawQuadTex(this.background, 0, 0, 8192, HEIGHT);
        drawQuadTex(this.background, -8192, 0, 8192, HEIGHT);
        glTranslatef(
                -(this.offset * Boot.alpha + this.oldOffset * (1 - Boot.alpha)),
                0, 0);
        drawQuadTex(this.background, 8192, 0, 8192, HEIGHT);
        this.ui.draw();
    }
}
