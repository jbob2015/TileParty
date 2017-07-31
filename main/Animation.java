package main;

import static helpers.Handler.*;

import java.util.*;

import helpers.Timer;

public class Animation {
    //private SpriteSheet sheet;
    protected ArrayList<Texture> animation;
    protected Timer timer;
    protected int currentFrame;
    protected double scale = 1;
    protected double fps = 3;

    /**
     * Default FPS = 3, Scale = 1
     */
    public Animation() {
        this.setFps(this.fps);
        //this.sheet = new SpriteSheet(name, width, height);
        this.animation = new ArrayList<Texture>();
    }

    /**
     * Default Scale = 1
     *
     * @param fps
     *            FPS to be set
     */
    public Animation(double fps) {
        this.fps = fps;
        this.setFps(this.fps);
        //this.sheet = new SpriteSheet(name, width, height);
        this.animation = new ArrayList<Texture>();
    }

    /**
     *
     * @param fps
     *            FPS to be set
     * @param scale
     *            scale to be set
     */
    public Animation(double fps, double scale) {
        this.fps = fps;
        this.setFps(this.fps);
        //this.sheet = new SpriteSheet(name, width, height);
        this.animation = new ArrayList<Texture>();
        this.scale = scale;
    }

    /**
     * Adds a frame to the end of the animation
     *
     * @param name
     */
    public void addFrame(String name) {
        this.animation.add(new Texture(name));
    }

    /**
     * Adds a frame at the specified index
     *
     * @param name
     *            file name of .png
     * @param index
     *            index inside animation
     */
    public void addFrame(String name, int index) {
        this.animation.add(index, new Texture(name));
    }

    /**
     * Gets the Texture object at the specified index
     *
     * @param index
     *            index to get texture from
     * @return texture at index
     */
    public Texture getTexture(int index) {
        assert index < this.animation.size();
        return this.animation.get(index);
    }

    /**
     * Gets the index of the current frame in the animation
     *
     * @return index of the current frame
     */
    public int getFrame() {
        return this.currentFrame;
    }

    /**
     * Sets the current frame to the specified index
     *
     * @param index
     *            index of new current frame
     */
    public void setFrame(int index) {
        assert index < this.animation.size();
        this.currentFrame = index;
    }

    /**
     * Draws the animation at the given x and y position
     *
     * @param x
     *            x position of animation
     * @param y
     *            y position of animation
     */
    public void draw(int x, int y) {
        drawQuadTex(this.animation.get(this.currentFrame), x, y,
                (int) (this.animation.get(this.currentFrame).getWidth()
                        * this.scale),
                (int) (this.animation.get(this.currentFrame).getHeight()
                        * this.scale));
    }

    /**
     * Updates the current frame if needed
     */
    public void update() {
        if (this.timer.canUpdate()) {
            this.currentFrame++;
            if (this.currentFrame >= this.animation.size()) {
                this.currentFrame = 0;
            }
        }
    }

    /**
     * Sets the FPS of the animation to the specified FPS
     *
     * @param fps
     *            FPS to be set
     */
    private void setFps(double fps) {
        this.fps = fps;
        this.timer = new Timer(fps);
    }

    /**
     * Restarts the animation from the first frame
     */
    public void restart() {
        this.currentFrame = 0;
    }
}
