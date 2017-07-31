package main.game.Bubbles;

import static helpers.Handler.*;

import helpers.*;
import main.*;

public class Bubble {

    private Source source = new Source();
    private int sound = loadSound("pop" + (int) (1 + 2 * Math.random()));
    public static int width = WIDTH * 128 / 1360;
    public static int height = HEIGHT * 128 / 768;
    public static int radius = WIDTH * 128 / 1360 / 2;
    private Texture bubble = new Texture("bubbleTrans");

    private float x, y, oldX, oldY, centerX, centerY;
    private float dx, dy;
    private Timer movementTimer = new Timer(60);
    public boolean popped = false;

    public Bubble(int x, int y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.centerX = this.x + width / 2;
        this.centerY = this.y + height / 2;
    }

    public void update() {
        this.oldX = this.x;
        this.oldY = this.y;
        this.movementTimer.canUpdate();
        if (this.x + this.dx * Boot.dt > WIDTH) {// - 30 * (Game.playerList.size() + 1)) {
            this.x = -Bubble.width;
            this.oldX = -Bubble.width;
        } else if (this.x + this.dx * Boot.dt < -Bubble.width) {
            this.x = WIDTH;
            this.oldX = WIDTH;
        }
        if (this.y + this.dy * Boot.dt < -Bubble.height) {
            this.y = HEIGHT - Bubble.height;
            this.oldY = HEIGHT - Bubble.height;
        }
        this.x += this.dx * Boot.dt;
        this.y += this.dy * Boot.dt;
        this.centerX = this.x + width / 2;
        this.centerY = this.y + height / 2;
    }

    public boolean pop(float x, float y) {
        if ((x - this.centerX) * (x - this.centerX)
                + (y - this.centerY) * (y - this.centerY) < Bubble.radius
                        * Bubble.radius) {
            this.popped = true;
            this.source.play(this.sound);
        }
        return this.popped;
    }

    public void draw() {
        if (!this.popped) {
            drawQuadTex(this.bubble,
                    (int) (this.x * Boot.alpha + this.oldX * (1 - Boot.alpha)),
                    (int) (this.y * Boot.alpha + this.oldY * (1 - Boot.alpha)),
                    Bubble.width, Bubble.width);
        }
    }
}
