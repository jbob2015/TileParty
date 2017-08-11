package main.game;

import static helpers.Handler.*;

import main.*;
import main.game.Game.*;
import serialization.*;

public class NetPlayer extends Player {

    public Cursor cursor;

    public NetPlayer(int id) {
        super(ClientPlayer.map, id);
        this.cursor = new Cursor(0, 0, id);
        this.bomb = new Bomb(this.x, this.y);
    }

    /**
     * Updates All Aspects of Player
     */
    public void update() {
        if (this.turnState != TurnState.WAITING) {
            this.boardPositionX = this.x + 24;
            this.boardPositionY = this.y + 96;
            this.updateTile();
            this.currentTile = Player.map.getTile(this.boardPositionX,
                    this.boardPositionY).type;
            this.updateAnimation();
        }
        this.idle.update();
    }

    /**
     * Updates the animation based on if the player is walking
     */
    private void updateAnimation() {
        if (this.turnState == TurnState.MOVING) {
            this.rightWalking.update();
            if (this.playStep) {
                source.play(step);
            }
        }
    }

    /**
     * Draws the player walking, or standing still. Also highlights the tile
     * below the player
     */
    public void draw() {
        if (this.turnState == TurnState.MOVING) {
            this.idle.restart();
            switch (this.toDirection) {
                case UP:
                    this.upWalking.draw(PIXELS * 5, PIXELS * 5);
                    break;
                case DOWN:
                    this.downWalking.draw(PIXELS * 5, PIXELS * 5);
                    break;
                case RIGHT:
                    this.rightWalking.draw(PIXELS * 5, PIXELS * 5); //Tested
                    break;
                default:
                    break;
            }
        } else {
            this.idle.draw(PIXELS * 5, PIXELS * 5);

            this.rightWalking.restart();
            this.upWalking.restart();
            this.downWalking.restart();
            this.updateTile();
        }
    }

    public static NetPlayer deserialize(TPObject player, TPObject mouse) {
        assert (player.getName().equals("Player"));
        int id = player.findField("ID").getInt();
        NetPlayer temp = new NetPlayer(id);
        temp.x = player.findField("x").getInt();
        temp.y = player.findField("y").getInt();
        temp.currentTile = TileType
                .create((player.findField("currentTile").getShort()));
        temp.turnState = TurnState
                .create(player.findField("turnState").getShort());
        temp.toDirection = Direction
                .create((player.findField("direction").getShort()));
        temp.playStep = player.findField("playStep").getBoolean();

        temp.cursor = new Cursor(mouse.findField("x").getInt(),
                mouse.findField("y").getInt(), id);

        return temp;
    }

}
