package main.game;

import static helpers.Handler.*;

import helpers.*;
import main.*;
import main.game.Game.*;

public abstract class Player {

    public int userID;

    public static TileGrid map;

    public int x = 0, y = HEIGHT - 64 - 64 - 64;
    public int boardPositionX, boardPositionY;
    public TileType currentTile, lastTile;

    public static Source source = new Source();
    public static int step = loadSound("step");

    public Timer stepTimer = new Timer(4);

    public Animation walking = new Animation(9, 4);

    public int coins;

    public Bomb bomb;
    public boolean canKnockBack = false;

    public TurnState turnState = TurnState.WAITING;

    public Player(TileGrid map, int id) {
        this.userID = id;
        Player.map = map;
    }

    protected void updateTile() {
        this.lastTile = this.currentTile;
    }

    @Override
    public String toString() {
        return "Player #" + this.userID + " located at (" + this.x + ", "
                + this.y + ")";
    }
}
