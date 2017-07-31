package main.game;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import main.*;
import main.game.Game.*;
import serialization.*;
import ui.*;

public class ClientPlayer extends Player {

    public UI ui = new UI();
    public int distanceToGo = 0;

    //public Texture highlight = loadTex("highlight");

    public ClientPlayer(TileGrid map, int id) {
        super(map, id);
        if (this.userID < 5) {
            this.walking.addFrame("rightWalking" + this.userID + "-1");
            this.walking.addFrame("rightWalking" + this.userID + "-2");
        } else {
            this.walking.addFrame("rightWalking1");
            this.walking.addFrame("rightWalking2");
        }
        this.currentTile = Player.map.getTile(this.boardPositionX,
                this.boardPositionY).type;
        this.updateTile();
        this.ui.addButton("roll", WIDTH / 2 - 64, HEIGHT / 4, .5);
        this.bomb = new Bomb(-16 + PIXELS * 15, this.y + 16);
    }

    /**
     * Updates All Aspects of Player
     */
    public void update() {
        if (this.turnState != TurnState.WAITING) {
            this.boardPositionX = PIXELS * 15 + this.x;
            this.boardPositionY = this.y + 128;
            this.currentTile = Player.map.getTile(this.boardPositionX,
                    this.boardPositionY).type;
            this.updateInput();
            this.updateMotion();
        }
    }

    /**
     * Updates the animation based on if the player is walking, and if the dice
     * is rolling
     */
    private void updateMotion() {
        int oldTile = (PIXELS * 15 + this.x) / PIXELS;
        if (this.turnState == TurnState.ROLLING
                && !StateManager.game.dice.isRolling) {
            this.turnState = TurnState.MOVING;
        }
        if (this.turnState == TurnState.MOVING) {
            this.walking.update();
            int dx = (int) (.15 * Boot.dt);
            if (dx > this.distanceToGo) {
                this.x += this.distanceToGo;
                this.distanceToGo = 0;
            } else {
                this.x += dx;
                this.distanceToGo -= dx;
            }
        }
        int newTile = (PIXELS * 15 + this.x) / PIXELS;

        if (this.turnState == TurnState.MOVING && oldTile != newTile) {
            source.play(step);
        }
    }

    /**
     * Updates All Possible Input (UI, Buttons, Mouse), Processes Actions
     */
    private void updateInput() {
        if (Keyboard.isClicked(Key.ESCAPE) || Keyboard.isClicked(Key.E)) {
            StateManager.changeState(GameState.GAMEPAUSED);
        } else if (this.turnState == TurnState.DECIDING) {
            if (this.ui.getButton("roll").isClicked()) {
                this.distanceToGo = StateManager.game.roll() * PIXELS;
                this.turnState = TurnState.ROLLING;
            }
        } else if (this.distanceToGo <= 0) {
            if (this.turnState != TurnState.ENDING) {
                this.turnState = TurnState.ENDING;
            } else {
                this.turnState = TurnState.WAITING;
            }
        }

    }

    /**
     * Draws the player walking, or standing still. Also highlights the tile
     * below the player
     */
    public void draw() {
        if (this.turnState == TurnState.MOVING) {
            this.walking.draw(-48 + PIXELS * 15, this.y); //Tested
        } else {
            drawQuadTex(this.walking.getTexture(0), -48 + PIXELS * 15, this.y,
                    this.walking.getTexture(0).getWidth() * 4,
                    this.walking.getTexture(0).getHeight() * 4);
            this.walking.restart();
            this.updateTile();
        }
        if (this.turnState == TurnState.DECIDING) {
            this.ui.draw();
        }
//        drawQuadTex(this.highlight, (PIXELS * 15) / PIXELS * PIXELS,
//                (this.y + this.walking.getTexture(0).getHeight() * 4) / PIXELS
//                        * PIXELS,
//                PIXELS, PIXELS);

    }

    public void serialize(TPObject object) {
        object.setName("Player");
        object.addField(TPField.Integer("ID", this.userID));
        object.addField(TPField.Integer("x", this.x));
        object.addField(TPField.Short("turnState", this.turnState.id));
    }

}
