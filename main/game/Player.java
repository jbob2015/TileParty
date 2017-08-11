package main.game;

import static helpers.Handler.*;

import helpers.*;
import main.*;
import main.game.Game.*;

public abstract class Player {

    public int userID;

    public static TileGrid map;

    public int x = 0, y = PIXELS * 32;
    public int boardPositionX, boardPositionY;
    public TileType currentTile, lastTile;

    public static Source source = new Source();
    public static int step = loadSound("step");
    public boolean playStep = false;

    public Timer stepTimer = new Timer(4);

    public Animation rightWalking = new Animation(8, 4);
    public Animation upWalking = new Animation(8, 4);
    public Animation downWalking = new Animation(8, 4);
    public Animation idle = new Animation(2, 4);

    public int coins;

    public Bomb bomb;
    public boolean canKnockBack = false;

    public TurnState turnState = TurnState.WAITING;
    public Direction toDirection = Direction.RIGHT;
    public Direction fromDirection = Direction.LEFT;

    public Player(TileGrid map, int id) {
        this.userID = id;
        Player.map = map;
        if (this.userID < 7 && this.userID > 0) {
            this.rightWalking.addFrame("rightWalking" + this.userID + "-1");
            this.rightWalking.addFrame("rightWalking" + this.userID + "-2");
            this.upWalking.addFrame("upWalking" + this.userID + "-1");
            this.upWalking.addFrame("upWalking" + this.userID + "-2");
            this.downWalking.addFrame("downWalking" + this.userID + "-1");
            this.downWalking.addFrame("downWalking" + this.userID + "-2");
            this.idle.addFrame("idle" + this.userID + "-1");
            this.idle.addFrame("idle" + this.userID + "-2");
        }
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
