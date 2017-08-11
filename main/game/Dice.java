package main.game;

import static helpers.Handler.*;

import main.*;

public class Dice {

    private Animation dice = new Animation(20, 4);
    public boolean isRolling;
    public int roll;
    public int x;
    public int y;
    public double scale = 4;

    public long lastTime;
    public static int sound = loadSound("diceRoll");
    public static Source source = new Source();

    public Dice(int x, int y) {
        for (int i = 1; i <= 6; i++) {
            this.dice.addFrame("dice" + i);
        }
        this.roll = (int) (1 + 6 * Math.random()); //Must Occur
        this.x = x;
        this.y = y;
        this.dice.setFrame(this.roll - 1);
        this.dice.scale = 8;
    }

    public Dice(int x, int y, double scale) {
        for (int i = 1; i <= 6; i++) {
            this.dice.addFrame("dice" + i);
        }
        this.roll = (int) (1 + 6 * Math.random()); //Must Occur
        this.x = x;
        this.y = y;
        this.dice.setFrame(this.roll - 1);
        this.dice.scale = scale;
    }

    /**
     * Updates the animation based on if the dice is rolling
     */
    public void update() {
        if (this.isRolling
                && this.lastTime > System.currentTimeMillis() - 1000) {
            this.dice.update();
        } else {
            this.isRolling = false;
            this.dice.setFrame(this.roll - 1);
        }
        this.draw();
    }

    /**
     * Draws the dice at the specified (x,y)
     */
    public void draw() {
        this.dice.draw(this.x, this.y);
    }

    /**
     * Rolls the dice and updates appropriate variables
     *
     * @return the value of the roll (int)
     */
    public int roll() {
        this.isRolling = true;
        this.roll = (int) (1 + 6 * Math.random());
        this.lastTime = System.currentTimeMillis();
        source.play(sound);
        return this.roll;
    }

}
