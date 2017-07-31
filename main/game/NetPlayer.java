package main.game;

import static helpers.Handler.*;

import main.game.Game.*;
import serialization.*;

public class NetPlayer extends Player {

    public Cursor cursor;

    public NetPlayer(int id) {
        super(ClientPlayer.map, id);
        if (this.userID < 5) {
            this.walking.addFrame("rightWalking" + this.userID + "-1");
            this.walking.addFrame("rightWalking" + this.userID + "-2");
        } else {
            this.walking.addFrame("rightWalking1");
            this.walking.addFrame("rightWalking2");
        }
        this.cursor = new Cursor(0, 0, id);
        this.bomb = new Bomb(this.x, this.y);
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
            this.updateAnimation();
        }
    }

    /**
     * Updates the animation based on if the player is walking
     */
    private void updateAnimation() {
        if (this.turnState == TurnState.MOVING) {
            this.walking.update();
            if (this.stepTimer.canUpdate()) {
                source.play(step);
            } //TODO SOUNDS NOT THE SAME
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
        this.cursor.draw(); //TODO Not drawing for some reason?
    }

    public static NetPlayer deserialize(TPObject player, TPObject mouse) {
        assert (player.getName().equals("Player"));
        int id = player.findField("ID").getInt();
        NetPlayer temp = new NetPlayer(id);
        temp.x = player.findField("x").getInt();
        temp.turnState = TurnState
                .create(player.findField("turnState").getShort());

        temp.cursor = new Cursor(mouse.findField("x").getInt(),
                mouse.findField("y").getInt(), id);

        return temp;
    }

}
