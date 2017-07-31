package main.game;

import static helpers.Handler.*;

import main.*;

public class Bomb {

    private Animation bomb = new Animation(13 / 1.947, 2);
    public boolean isExploding;
    public int x, y;

    private static int sound = loadSound("bomb");
    private static Source source = new Source();

    public Bomb(int x, int y) {

        for (int i = 0; i < 13; i++) {
            this.bomb.addFrame("BombExploding" + i);
        }
        this.bomb.addFrame("null");
        this.x = x;
        this.y = y;
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
        if (this.isExploding) {
            this.bomb.draw(this.x, this.y);
        }
    }

    public void explode() {
        this.isExploding = true;
        source.play(sound);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
