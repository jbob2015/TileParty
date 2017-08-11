package main;

import static helpers.Handler.*;

import java.util.*;

import helpers.Timer;

public class Animation {
    //private SpriteSheet sheet;
    protected ArrayList<Texture> animation = new ArrayList<Texture>();
    protected Timer timer;
    protected int currentFrame;
    public double scale = 1;
    protected double fps = 3;

    protected static int currentBirdFrame = 0;
    protected boolean isBird = false;
    protected static Timer birdTimer = new Timer(8);

    /**
     * Default FPS = 3, Scale = 1
     */
    public Animation() {
        this.setFps(this.fps);
        //this.sheet = new SpriteSheet(name, width, height);
    }

    /**
     * Default Scale = 1
     *
     * @param fps
     *            FPS to be set
     */
    public Animation(double fps) {
        this.setFps(fps);
        //this.sheet = new SpriteSheet(name, width, height);
    }

    /**
     *
     * @param fps
     *            FPS to be set
     * @param scale
     *            scale to be set
     */
    public Animation(double fps, double scale) {
        this.setFps(fps);
        //this.sheet = new SpriteSheet(name, width, height);
        this.scale = scale;
    }

    public Animation(TileType type) {
        switch (type) {
            case WaterFall0:
                this.setFps(8);
                this.scale = 4; //TODO CONSTANT SCALE
                this.addFrame("waterfall01");
                this.addFrame("waterfall02");
                this.addFrame("waterfall03");
                this.addFrame("waterfall04");
                break;
            case WaterFall1:
                this.setFps(8);
                this.scale = 4; //TODO CONSTANT SCALE
                this.addFrame("waterfall11");
                this.addFrame("waterfall12");
                this.addFrame("waterfall13");
                this.addFrame("waterfall14");
                break;
            case Bees:
                this.setFps(8);
                this.scale = 4; //TODO CONSTANT SCALE
                this.addFrame("bees");
                this.addFrame("bees1");
                this.addFrame("bees2");
                this.addFrame("bees3");
                this.addFrame("bees4");
                this.addFrame("bees5");
                break;
            case TreeBird:
                this.setFps(8);
                this.scale = 4; //TODO CONSTANT SCALE
                this.addFrame("treeBird");
                this.addFrame("treeBird1");
                this.addFrame("treeBird2");
                this.addFrame("treeBird3");
                this.addFrame("treeBird4");
                this.addFrame("treeBird5");
                this.addFrame("treeBird6");
                this.isBird = true;
                break;
            case Bird:
                this.setFps(8);
                this.scale = 4; //TODO CONSTANT SCALE
                this.addFrame("bird");
                this.addFrame("bird1");
                this.addFrame("bird2");
                this.addFrame("bird3");
                this.addFrame("bird4");
                this.addFrame("bird5");
                this.addFrame("bird6");
                this.isBird = true;
                break;
            default:
                break;
        }
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
        if (!this.isBird) {
            return this.currentFrame;
        } else {
            return currentBirdFrame;
        }
    }

    /**
     * Sets the current frame to the specified index
     *
     * @param index
     *            index of new current frame
     */
    public void setFrame(int index) {
        assert index < this.animation.size();
        if (!this.isBird) {
            this.currentFrame = index;
        } else {
            currentBirdFrame = index;
        }
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
        if (!this.isBird) {
            drawQuadTex(this.animation.get(this.currentFrame), x, y,
                    (int) (this.animation.get(this.currentFrame).getWidth()
                            * this.scale),
                    (int) (this.animation.get(this.currentFrame).getHeight()
                            * this.scale));
        } else {
            drawQuadTex(this.animation.get(currentBirdFrame), x, y,
                    (int) (this.animation.get(currentBirdFrame).getWidth()
                            * this.scale),
                    (int) (this.animation.get(currentBirdFrame).getHeight()
                            * this.scale));
        }
    }

    /**
     * Updates the current frame if needed
     */
    public void update() {
        if (!this.isBird) {
            if (this.timer.canUpdate()) {
                this.currentFrame++;
                if (this.currentFrame >= this.animation.size()) {
                    this.currentFrame = 0;
                }
            }
        } else {
            if (birdTimer.canUpdate()) {
                currentBirdFrame++;
                if (currentBirdFrame >= this.animation.size()) {
                    currentBirdFrame = 0;
                }
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
        if (!this.isBird) {
            this.timer = new Timer(fps);
        } else {
            birdTimer = new Timer(fps);
        }
    }

    /**
     * Restarts the animation from the first frame
     */
    public void restart() {
        if (!this.isBird) {
            this.currentFrame = 0;
        } else {
            currentBirdFrame = 0;
        }

    }
}
