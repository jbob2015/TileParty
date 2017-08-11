package main.game.Bombed;

import static helpers.Handler.*;

import helpers.*;
import main.*;
import main.game.*;
import main.game.Bombed.Bomb.*;

public abstract class BombedPlayer {

    public enum PlayerState {
        STANDING(0), LEFT(1), RIGHT(2), DEAD(3);
        public short id;

        PlayerState(int id) {
            this.id = (short) id;
        }

        static PlayerState create(int id) {
            switch (id) {
                case 0:
                    return STANDING;
                case 1:
                    return LEFT;
                case 2:
                    return RIGHT;
                case 3:
                    return DEAD;
            }
            return null;
        }
    }

    public int x = WIDTH / 4 + (int) (WIDTH / 2 * Math.random()),
            y = HEIGHT - 64 - 64 - 64;

    public Player player;
    public static Source source = new Source();
    public static int step = loadSound("step");
    public Timer stepTimer = new Timer(4);
    public Animation leftWalking = new Animation(10, 4);
    public Animation standing = new Animation(5, 4);
    public PlayerState state = PlayerState.STANDING;

    public Texture dead;

    public BombedPlayer(Player player) {
        this.player = player;
        if (this.player.userID < 7 && this.player.userID > 0) {
            this.dead = new Texture("dead" + this.player.userID);
            this.leftWalking
                    .addFrame("leftWalking" + this.player.userID + "-1");
            this.leftWalking
                    .addFrame("leftWalking" + this.player.userID + "-2");
            this.standing.addFrame("idle" + this.player.userID + "-1");
            this.standing.addFrame("idle" + this.player.userID + "-2");
        }
    }

    public void draw() {
        switch (this.state) {
            case LEFT:
                this.leftWalking.update();
                this.leftWalking.draw(-24 + this.x, this.y);
                break;
            case RIGHT:
                this.player.rightWalking.update();
                this.player.rightWalking.draw(-24 + this.x, this.y);
                break;
            case STANDING:
                this.standing.update();
                this.standing.draw(-24 + this.x, this.y);
                this.leftWalking.restart();
                this.player.rightWalking.restart();
                break;
            case DEAD:
                drawQuadTex(this.dead, -24 + this.x, this.y, 64, 128);
                break;
            default:
                break;
        }
    }

    public void kill(BombLocation side) {
        if (side.equals(BombLocation.LEFT)) {
            if (24 + this.x < WIDTH / 2) {
                this.state = PlayerState.DEAD;
            }
        } else {
            if (24 + this.x >= WIDTH / 2) {
                this.state = PlayerState.DEAD;
            }
        }
        if (this.state != PlayerState.DEAD) {
            this.player.coins += 10;
        }
    }

    public boolean isAlive() {
        return this.state != PlayerState.DEAD;
    }

    public void reset() {
        this.x = WIDTH / 4 + (int) (WIDTH / 2 * Math.random());
        this.y = HEIGHT - 64 - 64 - 64;
        this.state = PlayerState.STANDING;
    }

}
