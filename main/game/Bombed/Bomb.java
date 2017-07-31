package main.game.Bombed;

import static helpers.Handler.*;

import helpers.*;
import main.*;

public class Bomb {

    public Animation bomb = new Animation(13 / 1.947, 2);
    private boolean isExploding;
    private int x, y;
    private double scale;
    private int bombHeight;

    private static int sound = loadSound("bomb");
    private static Source source = new Source();

    private Timer bombTimer = new Timer(60);
    private boolean isDropping;
    public BombLocation side;

    public Bomb(int x) {
        Mouse.hideCursor();
        for (int i = 0; i < 13; i++) {
            this.bomb.addFrame("BombExploding" + i);
        }
        this.bomb.addFrame("null");
        this.x = x;
        this.bombHeight = (int) (this.bomb.getTexture(0).getHeight()
                * this.scale);
        this.y = (int) (-.90 * (this.bombHeight));

    }

    public Bomb(int x, double scale) {
        this.x = x;
        this.scale = scale;
        this.bomb = new Animation(13 / 1.947, this.scale);
        for (int i = 0; i < 13; i++) {
            this.bomb.addFrame("BombExploding" + i);
        }
        this.bomb.addFrame("null");
        this.bombHeight = (int) (this.bomb.getTexture(0).getHeight()
                * this.scale);
        this.y = (int) (-.90 * (this.bombHeight));
    }

    public void update() {
        if (this.isExploding && this.bomb.getFrame() < 13) {
            this.bomb.update();
        } else {
            this.isExploding = false;
            this.bomb.restart();
        }
        this.draw();
    }

    /**
     * Draws the dice at the specified (x,y)
     */
    public void draw() {
        int y = (int) (-.89 * (this.bombHeight));
        if (this.isDropping) {
            this.bombTimer.canUpdate();
            if (this.y >= y) {
                if (this.y < HEIGHT - this.bombHeight + 48) {
                    this.y += 2 * this.bombTimer.deltaMillis();
                } else {
                    this.y = HEIGHT - this.bombHeight + 48;
                    this.isDropping = false;
                }
            } else {
                this.y = y;
                source.play(sound);
                this.isExploding = true;
                this.bomb.restart();
            }
        }
        if (this.isExploding) {
            this.bomb.update();
            this.bomb.draw(this.x, this.y);
        }
    }

    public enum BombLocation {
        LEFT, RIGHT
    }

    public BombLocation explode() {
        this.y = (int) (-.90 * (this.bombHeight));
        this.isDropping = true;
        if (this.side == BombLocation.LEFT) {
            this.x = WIDTH / 16;
        } else {
            this.x = 9 * WIDTH / 16;
        }
        return this.side;

    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    void setY(int y) {
        this.y = y;
    }

    public boolean isExploding() {
        return this.isExploding;
    }

    public void explode(BombLocation side) {
        this.side = side;
        this.explode();
    }
}
