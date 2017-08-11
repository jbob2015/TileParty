package main;

import static helpers.Handler.*;

public class Tile {

    public int x, y;
    public Texture texture;
    public TileType type;
    public boolean editable = true;
    public Animation animation;

    public Tile(int x, int y, TileType type) {
        this.setX(x);
        this.setY(y);
        this.setType(type);
        if (!type.animated) {
            this.setTexture(new Texture(type.textureName));
        } else {
            this.animation = new Animation(type);
        }
    }

    /**
     * Draws the Tile based on its (x, y) on the grid
     */
    public void draw() {
        if (!this.type.equals(TileType.Null)) {
            if (!this.type.animated) {
                drawQuadTex(this.texture, this.x * PIXELS, this.y * PIXELS,
                        PIXELS, PIXELS);
            } else {
                this.animation.update();
                this.animation.draw(this.x * PIXELS, this.y * PIXELS);
            }
        }
    }

    /**
     * Sets x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Sets TileType
     */
    public void setType(TileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

}
